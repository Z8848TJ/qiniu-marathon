package com.paper.sword.controller.video;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paper.sword.common.util.PaperSwordUtil;
import com.paper.sword.common.vo.*;
import com.paper.sword.config.LabelConfig;
import com.paper.sword.config.QiniuConfig;
import com.paper.sword.getLable;
import com.paper.sword.video.RecommendedService;
import com.paper.sword.video.VideoEsService;
import com.paper.sword.video.VideoService;
import com.paper.sword.common.entity.Video;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {

    @Autowired
    private QiniuConfig qiniuConfig;

    @Autowired
    private LabelConfig labelconfig;
    
    @Reference
    private VideoService videoService;

    @Reference
    private VideoEsService esService;

    @Reference
    private RecommendedService recommendedService;



    @GetMapping("/upload")
    public Result upload() {
        // 生成上传凭证
        String filename = PaperSwordUtil.generateUUID();
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        
        // 回调参数
        JSONObject json = new JSONObject();
        json.put("key", "$(key)");
        json.put("fsize", "${fsize}");
        json.put("userId", UserHolder.getUser().getId());

        StringMap policy = new StringMap();
        policy.put("callbackUrl", qiniuConfig.getCallbackUrl());
        policy.put("returnBody", json.toJSONString());
        policy.put("callbackBodyType", "application/json");

        String token = auth.uploadToken(qiniuConfig.getVideoBucketName(), filename, 3600, policy);

        return Result.success().data(token);
    }

    
    @PostMapping("/callbackUrl")
    public String uploadCallback(HttpServletRequest request, HttpServletResponse response) {
       // 处理通知参数 
        String body = PaperSwordUtil.readDataFromHttp(request);
        log.info("回调通知信息 ==> {}", body);
        
        HashMap bodyMap = JSON.parseObject(body, HashMap.class);
        String fileName = (String)bodyMap.get("key");
        String size = (String)bodyMap.get("fsize");
        String userId = (String)bodyMap.get("useId");
        
        Result res;
        if(Integer.parseInt(size) > 0) {
            String lable = getLable.getLable(labelconfig.scriptPath,qiniuConfig.getVideoBucketUrl()+fileName, labelconfig.outputDir);
            // 将视频信息保存到数据库
            Video video = new Video();
            video.setId(UUID.randomUUID().toString());
            video.setVideoType(lable);
            video.setVideoUrl(fileName);
            video.setCreateTime(new Date());
            video.setUserId(userId);
            log.info("上传视频信息 ==> {}", video);
            videoService.save(video);
            EsVideo esVideo = new EsVideo();
            esVideo.setId(video.getId());
            esVideo.setVideoType(video.getVideoType());
            esVideo.setTitle(video.getTitle());
            esVideo.setDescription(video.getDescription());
            esVideo.setCreateTime(video.getCreateTime());
            esService.saveEsVideo(esVideo);


            response.setStatus(200);
            res = Result.success().data("文件上传成功");
        } else {
            response.setStatus(500);
            res = Result.error().data("文件上传失败");
        }
        
        return JSON.toJSONString(res);
    }

    /**
     * 获取用户上传列表
     */
    @GetMapping("/list")
    public Result videoList() {
        
        List<Video> list =
                videoService.videoListByUserId(UserHolder.getUser().getId());
        return Result.success().data(list);
    }


    @GetMapping("/recommend")
    public Result recommend(@RequestParam() Integer userId) {
        List<Similarity> similarityByUser = recommendedService.getSimilarityByUser(userId);
        HashSet<Integer> strings = new HashSet<>();
        for (Similarity similarity : similarityByUser) {
            List<Integer> videoTypeByUserId = recommendedService.getVideoTypeByUserId(similarity.userTwo);
            for (Integer i : videoTypeByUserId) {
                strings.add(i);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer string : strings) {
            stringBuilder.append(string);
        }
        List<Video> esVideoByType = esService.getEsVideoByType(stringBuilder.toString());
        return Result.success().data(esVideoByType);
    }
    
}
