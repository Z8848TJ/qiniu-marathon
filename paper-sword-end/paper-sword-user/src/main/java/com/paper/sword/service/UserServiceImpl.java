package com.paper.sword.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.mapper.FollowMapper;
import com.paper.sword.user.UserService;
import com.paper.sword.mapper.UserMapper;
import com.paper.sword.user.entity.Follow;
import com.paper.sword.user.entity.User;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

@Service(interfaceClass = UserService.class, loadbalance = "roundrobin" )
public class UserServiceImpl extends ServiceImpl<UserMapper, User> 
        implements UserService {
    
}

