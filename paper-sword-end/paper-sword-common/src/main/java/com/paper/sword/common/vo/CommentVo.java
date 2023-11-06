package com.paper.sword.common.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author: zzh
 * @description:
 */
@Data
public class CommentVo implements Serializable {


    public Integer userId;

    public String videoId;


    public String content;

    public Integer recoverUserId;
    
    public String username;
    
    public String headerUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public String createTime;
    
}
