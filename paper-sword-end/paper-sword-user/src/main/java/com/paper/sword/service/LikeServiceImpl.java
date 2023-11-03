package com.paper.sword.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.common.annotation.ControlsLog;
import com.paper.sword.common.vo.LikeVideoVo;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.mq.producer.KafkaProducer;
import com.paper.sword.user.CommentService;
import com.paper.sword.user.entity.Like;
import com.paper.sword.mapper.LikeMapper;
import com.paper.sword.user.LikeService;
import com.paper.sword.user.entity.Message;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> 
        implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private KafkaProducer kafkaProducer;
    
    @Reference
    private CommentService commentService;

    @Override
    @ControlsLog()
    public void likeVideo(String videoId, Integer userId) {
        Like like = new Like();
        like.setVideoId(videoId);
        like.setUserId(userId);
        like.setType(0);
        likeMapper.insert(like);

        // 发送通知消息
        Message message = buildMessage(videoId, userId);
        kafkaProducer.sendInteractMessage(message);
    }

    @Override
    @ControlsLog(operateType = 1)
    public void collectVideo(String videoId, Integer userId) {
        Like like = new Like();
        like.setVideoId(videoId);
        like.setUserId(userId);
        like.setType(1);
        likeMapper.insert(like);
    }

    @Override
    @ControlsLog(operateType = 3)
    public void transfer(String videoId) {}

    @Override
    public List<Integer> videoInfoCount(String videoId) {

        List<Integer> list = new ArrayList<>();

        Integer likeCount = query()
                .eq("video_id", videoId)
                .eq("type", 0)
                .count();

        Integer collectCount = query()
                .eq("video_id", videoId)
                .eq("type", 1)
                .count();

        int commentCount = commentService.commentCount(videoId);
        
        list.add(likeCount);
        list.add(collectCount);
        list.add(commentCount);
        
        return list;
    }

    @Override
    @ControlsLog(operateType = 5)
    public void notLike(String videoId) {}

    @Override
    @ControlsLog(operateType = 6)
    public void report(String videoId) {}

    @Override
    public List<LikeVideoVo> getLikeVideo(Integer userId) {
        return likeMapper.getLikeVideo(userId);
    }

    @Override
    public List<LikeVideoVo> getCollectVideo(Integer userId) {
        return likeMapper.getCollectVideo(userId);
    }

    private Message buildMessage(String videoId, Integer userId) {
        Message message = new Message();
        message.setToId(UserHolder.getUser().getId());
        message.setFromId(userId);
        message.setCreateTime(new Date());
        // 0 - 关注
        message.setType(0);
        // 0-未读
        message.setStatus(0);
        // 额外信息 - 视频ID
        JSONObject json = new JSONObject();
        json.put("videoId", videoId);
        message.setExtend(json.toJSONString());

        return message;
    }
}
