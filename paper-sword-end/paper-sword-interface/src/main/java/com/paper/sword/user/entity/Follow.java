package com.paper.sword.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;


@TableName(value ="t_follow")
@Data
public class Follow implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER)
    private String id;

    /**
     * 用户 ID
     */
    private Integer userId;

    /**
     * 关联的用户 ID
     */
    private Integer followUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}