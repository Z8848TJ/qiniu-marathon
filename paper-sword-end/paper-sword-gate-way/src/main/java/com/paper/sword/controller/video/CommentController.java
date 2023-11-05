package com.paper.sword.controller.video;

import com.paper.sword.common.vo.Result;
import com.paper.sword.user.CommentService;
import com.paper.sword.user.entity.Comment;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * @author wwh
 * @date 2023/11/6
 */
@RestController
public class CommentController {
    @Reference
    private CommentService commentService;

    @PostMapping("addComment")
    public Result addComment(@RequestBody Comment comment){
        commentService.writeComment(comment);
        return Result.success().data("评论成功");
    }

    @GetMapping("getByVideo")
    public Result getByVideo(@RequestParam String video){
        return Result.success().data(commentService.getParentComment(video));
    }

    @GetMapping("getByComment")
    public Result getByParentComment(@RequestParam String ParentCommentId){
        return Result.success().data(commentService.getChildrenComment(ParentCommentId));
    }
}
