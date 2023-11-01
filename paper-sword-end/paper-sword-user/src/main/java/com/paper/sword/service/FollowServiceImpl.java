package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.user.entity.Message;
import com.paper.sword.mq.producer.KafkaProducer;
import com.paper.sword.user.entity.Follow;
import com.paper.sword.user.FollowService;
import com.paper.sword.mapper.FollowMapper;


import com.paper.sword.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService{
    
    @Resource
    private FollowMapper followMapper;
    @Resource
    private KafkaProducer kafkaProducer;

    @Override
    public void follow(Integer followUserId, Boolean isFollow) {
        // 获取登录用户
        Integer userId = UserHolder.getUser().getId();

        // 判断关注或取关
        if (isFollow) {
            // 关注 新增数据
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            save(follow);
            
            // 发送 MQ 消息
            Message message = buildMessage(follow);
            ListenableFuture<SendResult<String, Object>> future = 
                    kafkaProducer.sendInteractMessage(message);

            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

                @Override
                public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                    // 4.1 MQ 消息发送完成
                }

                @Override
                public void onFailure(Throwable throwable) {
                    // 4.2 MQ 消息发送失败
                }

            });
        } else {
            // 取关 删除数据
            remove(new QueryWrapper<Follow>()
                    .eq("user_id", userId)
                    .eq("follow_user_id", followUserId));
        }
    }

    
    @Override
    public boolean isFollow(Integer followUserId) {
        // 获取登录用户
        Integer userId = UserHolder.getUser().getId();

        Integer count = query().eq("user_id", userId)
                .eq("follow_user_id", followUserId)
                .count();
        
        return count > 0;
    }

    @Override
    public List<User> followList(Integer userId) {
        return followMapper.selectFollowList(userId);
    }

    private Message buildMessage(Follow follow) {
        Message message = new Message();
        message.setToId(follow.getUserId());
        message.setFromId(follow.getFollowUserId());
        message.setCreateTime(new Date());
        // 2- 关注
        message.setType(2);
        // 0-未读
        message.setStatus(0);
        
        return message;
    }
}




