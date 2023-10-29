package com.paper.sword.video;

import com.paper.sword.video.entity.Comment;
import java.util.List;

/**
 * @author wwh
 * @date 2023/10/26
 */
public interface CommentService {

    void writeComment(Comment comment);

    List<Comment> getParentComment(String videoId);

    List<Comment> getChildrenComment(String parentId);
}
