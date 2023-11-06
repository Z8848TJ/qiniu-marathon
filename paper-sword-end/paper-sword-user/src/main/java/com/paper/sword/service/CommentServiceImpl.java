package com.paper.sword.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paper.sword.common.annotation.ControlsLog;
import com.paper.sword.common.enumType.OperateType;
import com.paper.sword.common.util.PaperSwordUtil;
import com.paper.sword.mapper.CommentMapper;
import com.paper.sword.mq.producer.KafkaProducer;
import com.paper.sword.user.entity.Message;
import com.paper.sword.user.CommentService;
import com.paper.sword.user.entity.Comment;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wwh
 * @date 2023/10/26
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> 
        implements CommentService{
    
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private KafkaProducer kafkaProducer;
    @Override
    @ControlsLog(operateType = 4)
    public void writeComment(Comment comment) {
        comment.setId(PaperSwordUtil.generateUUID());
        commentMapper.insert(comment);
        
        // 发送通知消息
        Message message = buildMessage(comment);
        kafkaProducer.sendInteractMessage(message);
    }

    @Override
    public int commentCount(String videoId) {
        return query().eq("video_id", videoId).count();
    }

    @Override
    public List<Comment> getParentComment(String videoId) {
        return commentMapper.selectList(new QueryWrapper<Comment>().eq("video_id", videoId));
    }

    @Override
    public List<Comment> getChildrenComment(String parentId) {
        return commentMapper.selectList(new QueryWrapper<Comment>().eq("parent_id", parentId));
    }

    private Message buildMessage(Comment comment) {
        Message message = new Message();
        message.setToId(comment.getUserId());
        message.setFromId(comment.getRecoverUserId());
        message.setCreateTime(new Date());
        // 0 - 关注
        message.setType(4);
        // 0-未读
        message.setStatus(0);
        // 额外信息 - 视频ID，评论内容
        JSONObject json = new JSONObject();
        json.put("videoId", comment.getVideoId());
        json.put("content", comment.getContent());
        message.setExtend(json.toJSONString());
        
        return message;
    }
}
