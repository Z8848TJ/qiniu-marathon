package com.paper.sword.service.messages;

import com.paper.sword.common.vo.MessageVO;
import com.paper.sword.user.entity.Message;


/**
 * @author: zzh
 * @description:
 */
public interface IPushMessage {

    MessageVO buildMessage(Message message);
    
}
