package com.paper.sword.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wwh
 * @date 2023/10/25
 */
@TableName("t_log")
@Data
public class Log {

    @TableId(value = "id", type = IdType.ID_WORKER)
    public String id;

    public String userId;

    public String userName;

    public Integer controls;

    public String videoType;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date createTime;

}
