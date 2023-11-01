package com.paper.sword.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    
    private Integer id;
    
    private String username;
    
    private String email;
    
    private String password;
    
    // 验证码
    private String code;
}
