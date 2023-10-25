package com.paper.sword.service;

import com.paper.sword.user.UserService;
import com.paper.sword.mapper.UserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = UserService.class, loadbalance = "roundrobin" )
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
}

