package com.paper.sword.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.user.entity.Message;
import com.paper.sword.mapper.MessageMapper;
import org.apache.dubbo.config.annotation.Service;


@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> {

}




