package com.paper.sword.service.messages;

import com.paper.sword.user.entity.Message;


/**
 * @author: zzh
 * @description:
 */
public interface IPushMessage {
    
    String buildMessage(Message message);
    
}
