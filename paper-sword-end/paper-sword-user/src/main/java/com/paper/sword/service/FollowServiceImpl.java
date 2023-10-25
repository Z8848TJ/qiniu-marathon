package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.user.entity.Follow;
import com.paper.sword.user.FollowService;
import com.paper.sword.mapper.FollowMapper;

import org.apache.dubbo.config.annotation.Service;


@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService{

    @Override
    public void follow(String followUserId, Boolean isFollow) {
        // 获取登录用户
        String userId = UserHolder.getUser().getId();

        // 判断关注或取关
        if (isFollow) {
            // 关注 新增数据
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            save(follow);
        } else {
            // 取关 删除数据
            remove(new QueryWrapper<Follow>()
                    .eq("user_id", userId)
                    .eq("follow_user_id", followUserId));
        }
    }

    @Override
    public boolean isFollow(String followUserId) {
        // 获取登录用户
        String userId = UserHolder.getUser().getId();

        Integer count = query().eq("user_id", userId)
                .eq("follow_user_id", followUserId)
                .count();
        
        return count > 0;
    }
}




