package com.paper.sword.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.paper.sword.common.util.Constants;
import com.paper.sword.user.entity.Message;
import com.paper.sword.service.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author: zzh
 * @description: 消息监听器
 */
@Component
@Slf4j
public class InteractMessageListener {
    
    @Resource
    private MessageServiceImpl messageService;

    @KafkaListener(topics = {Constants.Topic.LIKE, Constants.Topic.COMMENT, 
            Constants.Topic.ATTENTION}, groupId = "interact")
    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment ack,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> optional = Optional.ofNullable(record.value());

        // 1. 判断消息是否存在
        if (!optional.isPresent()) {
            return;
        }

        // 2. 处理 MQ 消息
        try {
            // 转化对象
            Message message = JSON.parseObject((String) optional.get(), Message.class);

            // 将消息插入到消息表中
            messageService.save(message);

            // 3. 打印日志
            log.info("消费MQ消息，完成 topic：{} ", topic);

            // 4. 消息消费完成 手动提交位移
            ack.acknowledge();
        } catch (Exception e) {
            log.error("消费MQ消息，失败 topic：{} message：{}", topic, optional.get());
        }
    }
    
    
}
