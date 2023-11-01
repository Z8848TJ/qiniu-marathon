package com.paper.sword.service.messages.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paper.sword.common.vo.MessageVO;
import com.paper.sword.common.entity.Video;
import com.paper.sword.service.messages.IPushMessage;
import com.paper.sword.user.UserService;
import com.paper.sword.user.entity.Message;
import com.paper.sword.user.entity.User;
import com.paper.sword.video.VideoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * @author: zzh
 * @description:
 */
@Component
public class LikeMessage implements IPushMessage {

    @Reference
    private UserService userService;

    @Reference
    private VideoService videoService;
    
    @Override
    public MessageVO buildMessage(Message message) {
        MessageVO messageVO = new MessageVO();

        // 填充用户信息
        User fromUser = userService.getById(message.getFromId());
        messageVO.setUserInfo(JSONObject.toJSONString(fromUser));

        // 填充视频信息
        String extend = message.getExtend();
        JSONObject extendObject = JSON.parseObject(extend);
        Video video = videoService.getById((String) extendObject.get("videoId"));
        messageVO.setVideoInfo(JSONObject.toJSONString(video));

        messageVO.setType(message.getType());
        messageVO.setTime(message.getCreateTime());

        return messageVO;
    }
}
