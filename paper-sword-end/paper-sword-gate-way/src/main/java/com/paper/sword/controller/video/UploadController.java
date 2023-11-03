package com.paper.sword.controller.video;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paper.sword.common.entity.Video;
import com.paper.sword.common.util.PaperSwordUtil;
import com.paper.sword.common.vo.EsVideo;
import com.paper.sword.common.vo.Result;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.common.vo.UserVO;
import com.paper.sword.config.LabelConfig;
import com.paper.sword.config.QiniuConfig;
import com.paper.sword.getLable;
import com.paper.sword.user.UserService;
import com.paper.sword.user.entity.User;
import com.paper.sword.video.VideoEsService;
import com.paper.sword.video.VideoService;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * @author: zzh
 * @description: 文件上传
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    @Autowired
    private QiniuConfig qiniuConfig;

    @Autowired
    private LabelConfig labelConfig;

    @Reference
    private VideoService videoService;

    @Reference
    private VideoEsService esService;
    
    @Reference
    private UserService userService;
    
    
    @GetMapping("/video")
    public Result video() {
        return uploadFile(qiniuConfig.getVideoBucketName(), qiniuConfig.getVideoCallbackUrl());
    }
    
    @PostMapping("/callback/video")
    public String videoCallback(HttpServletRequest request, HttpServletResponse response) {
        // 处理通知参数 
        String body = PaperSwordUtil.readDataFromHttp(request);
        log.info("回调通知信息 ==> {}", body);

        HashMap bodyMap = JSON.parseObject(body, HashMap.class);
        String fileName = (String)bodyMap.get("key");
        String size = (String)bodyMap.get("fsize");
        String userId = (String)bodyMap.get("useId");

        Result res;
        if(Integer.parseInt(size) > 0) {
            String label = getLable.getLable(labelConfig.scriptPath,qiniuConfig.getVideoBucketUrl() + fileName, labelConfig.outputDir);
            // 将视频信息保存到数据库
            Video video = new Video();
            video.setId(PaperSwordUtil.generateUUID());
            video.setVideoType(label);
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
    
    @PostMapping("/callback/header")
    public String headerCallBack(HttpServletRequest request, HttpServletResponse response) {
        String body = PaperSwordUtil.readDataFromHttp(request);
        log.info("回调通知信息 ==> {}", body);

        HashMap bodyMap = JSON.parseObject(body, HashMap.class);
        String fileName = (String)bodyMap.get("key");
        Integer size = (Integer)bodyMap.get("fsize");
        String userId = (String)bodyMap.get("userId");

        Result res;
        // 保存用户头像信息
        if(size > 0) {
            User user = new User();
            user.setHeaderUrl(fileName);
            user.setId(Integer.parseInt(userId));
            userService.updateById(user);

            response.setStatus(200);
            res = Result.success().data("文件上传成功");
        } else {
            response.setStatus(500);
            res = Result.error().data("文件上传失败");
        }

        return JSON.toJSONString(res);
    }
    
    @GetMapping("/header")
    public Result header() {
        return uploadFile(qiniuConfig.getHeaderBucketName(), qiniuConfig.getHeaderCallbackUrl());
    }
    
    private Result uploadFile(String bucket, String callback) {

        // 生成上传凭证
        String filename = PaperSwordUtil.generateUUID();
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());

        // 回调参数
        JSONObject json = new JSONObject();
        json.put("key", "$(key)");
        json.put("fsize", "${fsize}");
        UserVO user = UserHolder.getUser();
        json.put("userId", user.getId());

        StringMap policy = new StringMap();
        policy.put("callbackUrl", qiniuConfig.getDomain() + callback);
        policy.put("callbackBody", json.toJSONString());
        policy.put("callbackBodyType", "application/json");

        String token = auth.uploadToken(bucket, filename, 3600, policy);

        return Result.success()
                .data("token", token)
                .data("key", filename);
    }
    
}