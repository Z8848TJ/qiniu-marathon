package com.paper.sword.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Data
public class Video {
    @TableId(value = "id", type = IdType.ID_WORKER)
    public String id;

    public String userId;

    public Integer videoType;

    public String videoUrl;

    public String like;

    public String collect;

    public String title;

    public String description;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date createTime;
}
