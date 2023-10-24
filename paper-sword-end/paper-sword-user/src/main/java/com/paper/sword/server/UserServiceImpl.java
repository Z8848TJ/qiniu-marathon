package com.paper.sword.server;

import com.paper.sword.mapper.UserTMapper;
import com.paper.sword.user.UserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = UserService.class, loadbalance = "roundrobin" )
public class UserServiceImpl implements UserService {

    @Autowired
    private UserTMapper userTMapper;
    
}

