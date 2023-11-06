package com.paper.sword.user;


import com.baomidou.mybatisplus.extension.service.IService;
import com.paper.sword.user.entity.Follow;
import com.paper.sword.user.entity.User;

import java.util.List;

public interface FollowService extends IService<Follow> {

    /**
     * 关注 or 取关
     * @param followUserId 关注者 ID
     * @param isFollow 关注 or 取关
     */
    void follow(Integer followUserId, Boolean isFollow);

    /**
     * 是否关注
     * @param followUserId 关注 ID
     * @return 是否关注
     */
    boolean isFollow(Integer followUserId);


    List<User> followList(Integer userId);

    int followCount(Integer id);

    int followedCount(Integer id);
}
