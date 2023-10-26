package com.paper.sword.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;


@TableName(value ="t_message")
@Data
public class Message implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 
     */
    private String fromId;

    /**
     * 
     */
    private String toId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Data
    public static class Extend {
        /**
         *  视频链接
         */
        private String videoId;

        /**
         *  评论内容
         */
        private String content;
    }
}

