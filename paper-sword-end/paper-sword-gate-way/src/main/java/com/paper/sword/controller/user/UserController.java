package com.paper.sword.controller.user;

import com.paper.sword.common.vo.LikeVideoVo;
import com.paper.sword.common.vo.Result;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.user.LikeService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {
    
    @Reference
    private LikeService likeService;

    /**
     * 用户喜欢视频列表
     */
    @GetMapping("/videoList")
    public Result videoList() {
        Integer id = UserHolder.getUser().getId();

        List<LikeVideoVo> videoList = likeService.getLikeVideo(id);
        
        return Result.success().data(videoList);
    }

    /**
     * 用户收藏视频列表
     */
    @GetMapping("/collectList")
    public Result collectList() {
        Integer id = UserHolder.getUser().getId();

        List<LikeVideoVo> videoList = likeService.getCollectVideo(id);

        return Result.success().data(videoList);
    }
    
}
