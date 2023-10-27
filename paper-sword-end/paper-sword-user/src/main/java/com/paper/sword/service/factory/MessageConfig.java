package com.paper.sword.service.factory;

import com.paper.sword.service.messages.IPushMessage;
import com.paper.sword.service.messages.impl.AttentionMessage;
import com.paper.sword.service.messages.impl.CommentMessage;
import com.paper.sword.service.messages.impl.LikeMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zzh
 * @description:
 */
public class MessageConfig {

    protected static Map<Integer, IPushMessage> messagesMap = new ConcurrentHashMap<>();
    
    @Autowired
    private LikeMessage likeMessage;
    
    @Autowired
    private CommentMessage commentMessage;

    @Autowired
    private AttentionMessage attentionMessage;
    
    @PostConstruct
    public void init() {
        messagesMap.put(0, likeMessage);
        messagesMap.put(1, commentMessage);
        messagesMap.put(2, attentionMessage);
        
    }
    
}
