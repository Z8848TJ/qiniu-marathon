package com.paper.sword.video;

import com.paper.sword.video.entit.Comment;
import java.util.List;

/**
 * @author wwh
 * @date 2023/10/26
 */
public interface CommentService {

    public void writeComment(Comment comment);

    public List<Comment> getParentComment(String videoId);

    public List<Comment> getChildrenComment(String parentId);
}
