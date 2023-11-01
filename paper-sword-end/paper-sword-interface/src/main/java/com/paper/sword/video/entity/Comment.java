package com.paper.sword.video.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wwh
 * @date 2023/10/26
 */
@Data
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
