package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.common.vo.MessageVO;
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
    public List<MessageVO> pushInteractMessage(Integer userId) {
        // 查询该用户的未读信息
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getStatus, 0);
        wrapper.eq(Message::getToId, userId);
        List<Message> messages = baseMapper.selectList(wrapper);

        ArrayList<MessageVO> list = new ArrayList<>();
        // 对不同的消息进行不同的处理
        for (Message message : messages) {
            Integer type = message.getType();
            IPushMessage messageService = messageFactory.getMessageService(type);
            list.add(messageService.buildMessage(message));
        }

        return list;
    }

    @Override
    public void updateStatusByIds(String ids) {
        messageMapper.updateStatusByIds(ids);
    }
}




