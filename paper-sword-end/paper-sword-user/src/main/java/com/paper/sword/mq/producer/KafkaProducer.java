package com.paper.sword.mq.producer;

import com.alibaba.fastjson.JSON;
import com.paper.sword.common.util.Constants;
import com.paper.sword.user.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zzh
 * @description: MQ 消息发送服务
 */
@Component
@Slf4j
public class KafkaProducer {

    public static final Map<Integer, String> TOPIC;

    /*
     * 0-点赞;1-评论;2-关注
     */
    static {
        TOPIC = new HashMap<>();
        TOPIC.put(0, Constants.Topic.LIKE);
        TOPIC.put(1, Constants.Topic.COMMENT);
        TOPIC.put(2, Constants.Topic.ATTENTION);
    }
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 发送中通知消息
     *
     * @param message 消息
     */
    public ListenableFuture<SendResult<String, Object>> sendInteractMessage(Message message) {
        String objJson = JSON.toJSONString(message);
        String topic = TOPIC.get(message.getType());
        log.info("发送MQ消息 topic：{} message：{}", topic, objJson);
        return kafkaTemplate.send(topic, objJson);
    }
    
    
    
}
