package com.paper.sword.service.messages.impl;

import com.alibaba.fastjson.JSONObject;
import com.paper.sword.service.messages.IPushMessage;
import com.paper.sword.user.UserService;
import com.paper.sword.user.entity.Message;
import com.paper.sword.user.entity.User;
import com.paper.sword.vo.MessageVO;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Component;

/**
 * @author: zzh
 * @description: 关注消息
 */
@Component
public class AttentionMessage implements IPushMessage {
    
    @Reference
    private UserService userService;
    
    @Override
    public String buildMessage(Message message) {
        MessageVO messageVo = new MessageVO();

        messageVo.setId(message.getId());
        messageVo.setType(message.getType());
        messageVo.setTime(message.getCreateTime());
        
        
        // 查询用户信息
        User user = userService.getById(message.getFromId());
        String userJson = JSONObject.toJSONString(user);
        messageVo.setUserInfo(userJson);
        
        return JSONObject.toJSONString(messageVo);
    }
}
