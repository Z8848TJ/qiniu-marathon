package com.paper.sword.user;


import com.baomidou.mybatisplus.extension.service.IService;
import com.paper.sword.user.entity.Follow;

public interface FollowService extends IService<Follow> {

    void follow(String followUserId, Boolean isFollow);

    boolean isFollow(String followUserId);
}
