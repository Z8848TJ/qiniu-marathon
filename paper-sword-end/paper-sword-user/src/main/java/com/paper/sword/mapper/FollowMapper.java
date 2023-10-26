package com.paper.sword.mapper;

import com.paper.sword.user.entity.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paper.sword.user.entity.User;

import java.util.List;


public interface FollowMapper extends BaseMapper<Follow> {

    List<User> selectFollowList(String userId);
}




