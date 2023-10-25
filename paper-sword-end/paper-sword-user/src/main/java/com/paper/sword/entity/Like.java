package com.paper.sword.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wwh
 * @date 2023/10/25
 */
@TableName("t_like")
@Data
public class Like {

    @TableId(value = "id", type = IdType.ID_WORKER)
    public String id;

    public String userId;

    public String videoId;

    public Integer type;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date createTime;
}
