package com.paper.sword.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("t_user")
@Data
public class User{

    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String email;

    /**
     * 密码
     */
    private String password;
    
    /**
     * 加密盐值
     */
    private String salt;
    
    /**
     * 头像Url
     */
    private String headerUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date createTime;
}

