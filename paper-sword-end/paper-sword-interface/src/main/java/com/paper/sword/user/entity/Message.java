package com.paper.sword.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


@TableName(value ="t_message")
@Data
public class Message implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    
    private Integer fromId;

    
    private Integer toId;

    /**
     * 0-点赞;1-评论;2-关注
     */
    private Integer type;

    /**
     * 额外信息 JSON 格式：video_id-视频ID，content-评论内容
     */
    private String extend;

    /**
     * 0-未读;1-已读;2-删除;
     */
    private Integer status;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

