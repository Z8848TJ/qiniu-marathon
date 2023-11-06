package com.paper.sword.controller.user;

import com.paper.sword.common.entity.Video;
import com.paper.sword.common.vo.LikeVideoVo;
import com.paper.sword.common.vo.Result;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.config.QiniuConfig;
import com.paper.sword.user.FollowService;
import com.paper.sword.user.LikeService;
import com.paper.sword.user.UserService;
import com.paper.sword.user.entity.User;
import com.paper.sword.video.VideoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {
    
    @Reference
    private LikeService likeService;
    
    @Reference
    private FollowService followService;
    
    @Reference
    private UserService userService;
    
    @Reference
    private VideoService videoService;
    
    @Autowired
    private QiniuConfig qiniuConfig;

    /**
     * 获取用户视频列表
     */
    @GetMapping("/videoList")
    public Result videoList() {

        List<Video> list =
                videoService.videoListByUserId(UserHolder.getUser().getId());

        return Result.success().data(list);
    }

    /**
     * 用户喜欢视频列表
     */
    @GetMapping("/likeList")
    public Result likeVideoList() {
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

    /**
     * 用户主页信息
     */
    @GetMapping("/mainInfo")
    public Result userInfo() {
        Integer id = UserHolder.getUser().getId();
        // 获赞数
        int likeCount = likeService.gainLikeCount(id);
        // 关注数
        int followCount = followService.followCount(id);
        // 粉丝数
        int followedCount = followService.followedCount(id);
        User user = userService.getById(id);
        user.setHeaderUrl(qiniuConfig.getHeaderBucketUrl() + user.getHeaderUrl());
        user.setSalt(null);
        user.setPassword(null);
        
        return Result.success().data("user", user)
                                .data("likeCount", likeCount)
                                .data("followCount", followCount)
                                .data("followedCount", followedCount);
    }

}
