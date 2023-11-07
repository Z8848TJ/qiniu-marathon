package com.paper.sword.controller.video;

import com.paper.sword.common.vo.Result;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.user.CommentService;
import com.paper.sword.user.entity.Comment;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author wwh
 * @date 2023/11/6
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Reference
    private CommentService commentService;

    @PostMapping("/add")
    public Result addComment(@RequestBody Comment comment){
        comment.setCreateTime(new Date());
        comment.setUserId(UserHolder.getUser().getId());
        commentService.writeComment(comment);
        return Result.success().data("评论成功");
    }
}
