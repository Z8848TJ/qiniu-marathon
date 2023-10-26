package com.paper.sword.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MessageVO {
    
    private String id;

    /**
     * 0-点赞;1-评论;2-关注
     */
    private Integer type;

    /**
     * 用户的 JSON 数据
     */
    private String userInfo;

    /**
     * 视频的 JSON 数据
     */
    private String videoInfo;

    /**
     * 评论信息
     */
    private String comment;

    /**
     * 消息触发时间
     */
    private Date time;
}
