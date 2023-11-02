package com.paper.sword.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Data
@TableName("t_video")
public class Video implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    public String id;

    public String userId;

    public String videoType;

    public String videoUrl;

    public String like;

    public String collect;

    public String title;

    public String cover;

    public String description;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date createTime;
}
