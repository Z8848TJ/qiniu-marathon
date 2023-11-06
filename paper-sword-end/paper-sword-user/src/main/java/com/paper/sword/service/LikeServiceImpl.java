package com.paper.sword.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.common.annotation.ControlsLog;
import com.paper.sword.common.enumType.OperateType;
import com.paper.sword.common.vo.LikeVideoVo;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.mq.producer.KafkaProducer;
import com.paper.sword.user.CommentService;
import com.paper.sword.user.entity.Like;
import com.paper.sword.mapper.LikeMapper;
import com.paper.sword.user.LikeService;
import com.paper.sword.user.entity.Message;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> 
        implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private KafkaProducer kafkaProducer;
    
    @Reference
    private CommentService commentService;

    @Override
    public void likeVideo(String videoId, Integer fromId, Integer toId, Integer type) {
        if(type == 1) {
            Like like = new Like();
            like.setVideoId(videoId);
            like.setUserId(fromId);
            like.setType(0);
            like.setCreateTime(new Date());
            likeMapper.insert(like);

            // 发送通知消息
            Message message = buildMessage(videoId, fromId, toId, 0);
            kafkaProducer.sendInteractMessage(message);
        } else {
            // 删除数据库记录
            likeMapper.delete(new QueryWrapper<Like>()
                            .eq("video_id", videoId)
                            .eq("user_id", fromId));
        }

    }

    @Override
    @ControlsLog(operateType = OperateType.collect)
    public void collectVideo(String videoId, Integer fromId,  Integer toId, Integer type) {
        if(type == 1) {
            Like like = new Like();
            like.setVideoId(videoId);
            like.setUserId(fromId);
            like.setType(1);
            like.setCreateTime(new Date());
            likeMapper.insert(like);

            // 发送通知消息
            Message message = buildMessage(videoId, fromId, toId, 1);
            kafkaProducer.sendInteractMessage(message);
        } else {
            // 删除数据库记录
            likeMapper.delete(new QueryWrapper<Like>()
                    .eq("video_id", videoId)
                    .eq("user_id", fromId));
        }
    }

    @Override
    public List<Boolean> isLikeAndIsCollect(String videoId, Integer userId) {
        boolean like = query().eq("video_id", videoId)
                .eq("user_id", userId)
                .eq("type", 0)
                .count() > 0;

        boolean collect = query().eq("video_id", videoId)
                .eq("user_id", userId)
                .eq("type", 1)
                .count() > 0;

        List<Boolean> res = new ArrayList<>();
        
        res.add(like);
        res.add(collect);

        log.info("是否点赞，收藏 ==> {}", res);
        
        return res;
    }

    @Override
    @ControlsLog(operateType = OperateType.retransmission)
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
        
        log.info("视频信息 ==> {}", list);
        
        return list;
    }

    @Override
    public Integer gainLikeCount(Integer userId) {
        return query().eq("user_id", userId)
                .eq("type", 0).count();
    }

    @Override
    @ControlsLog(operateType = OperateType.NotInterested)
    public void notLike(String videoId) {}

    @Override
    @ControlsLog(operateType = OperateType.report)
    public void report(String videoId) {}

    @Override
    public List<LikeVideoVo> getLikeVideo(Integer userId) {
        return likeMapper.getLikeVideo(userId);
    }

    @Override
    public List<LikeVideoVo> getCollectVideo(Integer userId) {
        return likeMapper.getCollectVideo(userId);
    }

    // 构造通知消息
    private Message buildMessage(String videoId, Integer fromId, Integer toId, Integer type) {
        Message message = new Message();
        message.setToId(toId);
        message.setFromId(fromId);
        message.setCreateTime(new Date());
        // 0 - 关注 1 - 收藏
        message.setType(type);
        // 0-未读
        message.setStatus(0);
        // 额外信息 - 视频ID
        JSONObject json = new JSONObject();
        json.put("videoId", videoId);
        message.setExtend(json.toJSONString());

        return message;
    }
}
