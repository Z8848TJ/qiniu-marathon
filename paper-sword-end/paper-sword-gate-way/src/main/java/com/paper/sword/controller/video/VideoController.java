package com.paper.sword.controller.video;

import com.paper.sword.common.vo.Result;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.user.LikeService;
import com.paper.sword.video.VideoService;
import com.paper.sword.common.entity.Video;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {
    
    @Reference
    private VideoService videoService;
    
    @Reference
    private LikeService likeService;
    
    
    /**
     * 获取用户视频列表
     */
    @GetMapping("/list")
    public Result videoList() {
        
        List<Video> list = 
                videoService.videoListByUserId(UserHolder.getUser().getId());
        
        return Result.success().data(list);
    }

    /**
     * 获取视频点赞数，收藏数，评论数
     */
    @GetMapping("/count")
    public Result listCount(@RequestParam String videoId) {
        List<Integer> counts = likeService.videoInfoCount(videoId);
        
        return Result.success().data(counts);
    }

}
