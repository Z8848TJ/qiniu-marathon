package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.service.factory.PushMessageFactory;
import com.paper.sword.service.messages.IPushMessage;
import com.paper.sword.user.MessageService;
import com.paper.sword.user.entity.Message;
import com.paper.sword.mapper.MessageMapper;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> 
implements MessageService {
    
    @Resource
    private MessageMapper messageMapper;
    
    @Resource
    private PushMessageFactory messageFactory;
    
    @Override
    public List<String> pushInteractMessage(String userId) {
        // 查询该用户的未读信息
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getStatus, 0);
        List<Message> messages = baseMapper.selectList(wrapper);

        ArrayList<String> list = new ArrayList<>();
        // 对不同的消息进行不同的处理
        for (Message message : messages) {
            Integer type = message.getType();
            IPushMessage messageService = messageFactory.getMessageService(type);
            String msgJson = messageService.buildMessage(message);
            list.add(msgJson);
        }
        
        return list;
    }

    @Override
    public void updateStatusByIds(String ids) {
        messageMapper.updateStatusByIds(ids);
    }
}




