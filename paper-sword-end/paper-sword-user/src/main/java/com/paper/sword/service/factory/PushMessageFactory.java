package com.paper.sword.service.factory;


import com.paper.sword.service.messages.IPushMessage;
import org.springframework.stereotype.Service;

/**
 * @author: zzh
 * @description: 推送消息工程
 */
@Service
public class PushMessageFactory extends MessageConfig {
    
    public IPushMessage getMessageService(int messageType) {
        return messagesMap.get(messageType);
    }
    
}
