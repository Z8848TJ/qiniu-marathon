package com.paper.sword.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;


@TableName(value ="t_video")
@Data
public class Video implements Serializable {
    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 视频类型
     */
    private Integer videoType;

    /**
     * 视频连接
     */
    private String videoUrl;

    /**
     * 点赞数量
     */
    private String like;

    /**
     * 收藏数量
     */
    private String collect;

    /**
     * 标题
     */
    private String title;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 发布时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}