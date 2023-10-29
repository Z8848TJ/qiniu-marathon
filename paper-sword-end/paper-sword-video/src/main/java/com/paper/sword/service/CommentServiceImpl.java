package com.paper.sword.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.paper.sword.common.annotation.ControlsLog;
import com.paper.sword.mapper.CommentMapper;
import com.paper.sword.video.CommentService;
import com.paper.sword.video.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wwh
 * @date 2023/10/26
 */
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Override
    @ControlsLog(operateType = 4)
    public void writeComment(Comment comment) {
        commentMapper.insert(comment);
    }

    @Override
    public List<Comment> getParentComment(String videoId) {
        return commentMapper.selectList(new QueryWrapper<Comment>().eq("video_id", videoId));
    }

    @Override
    public List<Comment> getChildrenComment(String parentId) {
        return commentMapper.selectList(new QueryWrapper<Comment>().eq("parent_id", parentId));
    }
}
