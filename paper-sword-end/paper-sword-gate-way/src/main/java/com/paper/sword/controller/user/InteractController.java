package com.paper.sword.controller.user;

import com.paper.sword.common.annotation.ControlsLog;
import com.paper.sword.common.enumType.OperateType;
import com.paper.sword.common.vo.Result;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.user.LikeService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

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
    @ControlsLog(operateType = OperateType.like)
    @GetMapping("/like")
    public Result like(@RequestParam String videoId, @RequestParam Integer userId, @RequestParam Integer type) {
        Integer fromId = UserHolder.getUser().getId();
        likeService.likeVideo(videoId, fromId, userId, type);
        return Result.success();
    }
    
    // 收藏
    @GetMapping("/collect")
    @ControlsLog(operateType = OperateType.collect)
    public Result collect(@RequestParam String videoId, @RequestParam Integer userId) {
        likeService.collectVideo(videoId, userId);
        return Result.success();
    }
    
    // 转发
    @GetMapping("transfer")
    @ControlsLog(operateType = OperateType.retransmission)
    public Result transfer(@RequestParam String videoId) {
        likeService.transfer(videoId);

        return Result.success();
    }
    
    // 不感兴趣
    @GetMapping("/notLike")
    @ControlsLog(operateType = OperateType.NotInterested)
    public Result notList(@RequestParam String videoId) {
        likeService.notLike(videoId);

        return Result.success();
    }
    
    // 举报
    @GetMapping("/report")
    @ControlsLog(operateType = OperateType.report)
    public Result report(@RequestParam String videoId) {
        likeService.report(videoId);

        return Result.success();
    }
    
    
}
