package com.paper.sword.service.messages.impl;

import com.paper.sword.service.messages.IPushMessage;
import com.paper.sword.user.entity.Message;
import org.springframework.stereotype.Component;

/**
 * @author: zzh
 * @description:
 */
@Component
public class LikeMessage implements IPushMessage {
    @Override
    public String buildMessage(Message message) {
        return null;
    }
}
