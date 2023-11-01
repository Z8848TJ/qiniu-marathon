package com.paper.sword.controller.user;

import com.paper.sword.common.vo.Result;
import com.paper.sword.user.LikeService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zzh
 * @description:
 */
@RestController
@RequestMapping("/interact")
public class InteractController {
    
    @Reference
    private LikeService likeService;
    
    // 点赞
    @GetMapping("/like")
    public Result like(@RequestParam String videoId, @RequestParam Integer userId) {
        likeService.likeVideo(videoId, userId);
        
        return Result.success();
    }
    
    // 收藏
    @GetMapping("/collect")
    public Result collect(@RequestParam String videoId, @RequestParam Integer userId) {
        likeService.collectVideo(videoId, userId);

        return Result.success();
    }
    
    // 转发
    @GetMapping("transfer")
    public Result transfer(@RequestParam String videoId) {
        likeService.transfer(videoId);

        return Result.success();
    }
    
    // 不感兴趣
    @GetMapping("/notLike")
    public Result notList(@RequestParam String videoId) {
        likeService.notLike(videoId);

        return Result.success();
    }
    
    // 举报
    @GetMapping("/report")
    public Result report(@RequestParam String videoId) {
        likeService.report(videoId);

        return Result.success();
    }
    
    
}
