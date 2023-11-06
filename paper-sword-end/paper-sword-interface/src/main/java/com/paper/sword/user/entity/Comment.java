package com.paper.sword.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wwh
 * @date 2023/10/26
 */
@Data
@TableName("t_comments")
public class Comment {

    @TableId(value = "id", type = IdType.ID_WORKER)
    public String id;

    public Integer userId;

    public String videoId;

    public String parentId;

    public String content;

    public Integer recoverUserId;

    public Integer likes;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date createTime;

}
