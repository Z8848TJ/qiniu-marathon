package com.paper.sword.controller.video;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paper.sword.common.entity.Dict;
import com.paper.sword.common.entity.Video;
import com.paper.sword.common.mapper.DictMapper;
import com.paper.sword.common.util.PaperSwordUtil;
import com.paper.sword.common.util.RedisUtil;
import com.paper.sword.common.vo.*;
import com.paper.sword.config.LabelConfig;
import com.paper.sword.config.QiniuConfig;
import com.paper.sword.GetLabel;
import com.paper.sword.user.UserService;
import com.paper.sword.user.entity.User;
import com.paper.sword.video.VideoEsService;
import com.paper.sword.video.VideoService;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

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

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private Upload upload;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    
    @GetMapping("/video")
    public Result video() {
        return uploadFile(qiniuConfig.getVideoBucketName(), qiniuConfig.getVideoCallbackUrl());
    }
    
    @PostMapping("/callback/video")
    public synchronized String videoCallback(HttpServletRequest request, HttpServletResponse response) {
        // 处理通知参数 
        String body = PaperSwordUtil.readDataFromHttp(request);
        log.info("回调通知信息 ==> {}", body);

        HashMap bodyMap = JSON.parseObject(body, HashMap.class);
        String fileName = (String)bodyMap.get("key");
        String size = (String)bodyMap.get("fsize");
        Integer userId = (Integer) bodyMap.get("userId");

        // 幂等性处理
        String url = qiniuConfig.getVideoBucketUrl() + fileName;
        Video videoOld = videoService.getVideoCountByUrl(url);
        Result res;

        if(videoOld != null) {
            String[] split = videoOld.getVideoType().split(",");
            StringBuilder stringBuilder = new StringBuilder();


            for (int i = 0; i < split.length - 1; i++) {
                Dict key = dictMapper.getValueByKey(Integer.parseInt(split[i]));
                stringBuilder.append(key.getValue()).append(",");
            }

            Dict key = dictMapper.getValueByKey(Integer.parseInt(split[split.length - 1]));
            stringBuilder.append(key.getValue());

            String videoTypeString = stringBuilder.toString();
            videoOld.setVideoString(videoTypeString);

            response.setStatus(200);
            res = Result.success().data(videoOld);
            return JSON.toJSONString(res);
        }

        log.info("视频大小 ==> {}", size);

        if(Integer.parseInt(size) > 0) {
            // 分析视频标签
            fileVo label = GetLabel.getLabel(labelConfig.scriptPath, qiniuConfig.getVideoBucketUrl() + fileName, labelConfig.outputDir);
            
            String labType = label.getType();
            // 未识别视频标签，默认为 10-其他
            if(StringUtils.isBlank(labType)) {
                labType = "10";
            }
            
            // 将视频标签解析为对应字符内容
            String[] split = labType.split(",");
            StringBuilder buffer = new StringBuilder();
            
            for (int i = 0; i < split.length - 1; i++) {
                Dict key = dictMapper.getValueByKey(Integer.parseInt(split[i]));
                buffer.append(key.getValue()).append(",");
            }
            Dict key = dictMapper.getValueByKey(Integer.parseInt(split[split.length - 1]));
            buffer.append(key.getValue());

            // 上传封面
            String coverUrl = upload.imageUpload(label.imagePath);
            
            // 保存视频信息
            Video video = new Video();
            String id = PaperSwordUtil.generateUUID();
            video.setId(id);
            video.setVideoType(labType);
            video.setVideoUrl(qiniuConfig.getVideoBucketUrl() + fileName);
            video.setCreateTime(new Date());
            video.setUserId(userId);
            video.setCover(qiniuConfig.getImageBucketUrl() + coverUrl);
            video.setVideoString(buffer.toString());
            log.info("上传视频信息 ==> {}", video);
            videoService.save(video);
            
            // 删除临时文件
            File folder = new File(labelConfig.outputDir);
            File[] files = folder.listFiles();
            for (File file : files) {
                if(file.isFile()){
                    file.delete();
                }
            }
            response.setStatus(200);
            res = Result.success().data(video);
        } else {
            response.setStatus(500);
            res = Result.error().data("文件上传失败");
        }

        return JSON.toJSONString(res);
    }

    /**
     * 获取视频信息
     */
    @PostMapping("/videoInfo")
    public Result videoInfo(@RequestBody Video video) {
        // 更新视频信息
        video.setUsername(UserHolder.getUser().getUsername());
        video.setCreateTime(new Date());
        videoService.updateById(video);

        // 将视频信息存入 es
        Video byId = videoService.getById(video.getId());
        EsVideo esVideo = new EsVideo();
        esVideo.setId(UUID.randomUUID().toString());
        esVideo.setVideoId(byId.getId());
        esVideo.setVideoType(byId.getVideoType());
        esVideo.setUsername(UserHolder.getUser().getUsername());
        esVideo.setDescription(byId.getDescription());
        esVideo.setCreateTime(byId.getCreateTime());
        esService.saveEsVideo(esVideo);

        // 计算视频分数
        String scoreKey = RedisUtil.getVideoScoreKey();
        redisTemplate.opsForSet().add(scoreKey, video.getId());

        return Result.success().data("投稿成功");
    }
    
    @PostMapping("/callback/header")
    public String headerCallBack(HttpServletRequest request, HttpServletResponse response) {
        String body = PaperSwordUtil.readDataFromHttp(request);
        log.info("回调通知信息 ==> {}", body);

        HashMap bodyMap = JSON.parseObject(body, HashMap.class);
        String fileName = (String)bodyMap.get("key");
        Integer size = (Integer)bodyMap.get("fsize");
        Integer userId = (Integer)bodyMap.get("userId");

        Result res;
        // 保存用户头像信息
        if(size > 0) {
            User user = new User();
            user.setHeaderUrl(fileName);
            user.setId(userId);
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
