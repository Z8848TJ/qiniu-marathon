package com.paper.sword.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.paper.sword.user.entity.Comment;
import java.util.List;

/**
 * @author wwh
 * @date 2023/10/26
 */
public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     */
    void writeComment(Comment comment);

    /**
     * 评论数
     */
    int commentCount(String videoId);

    
    List<Comment> getParentComment(String videoId);

    List<Comment> getChildrenComment(String parentId);
}
