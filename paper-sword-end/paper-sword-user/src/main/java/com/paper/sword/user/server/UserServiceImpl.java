package com.paper.sword.user.server;

import com.paper.sword.interfacer.user.UserService;
import com.paper.sword.user.mapper.UserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = UserService.class, loadbalance = "roundrobin" )
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
}

