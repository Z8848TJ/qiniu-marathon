package com.paper.sword.user;

import com.paper.sword.common.vo.UserVO;

public interface AuthService {

    /**
     * 发送邮件获取验证码
     * @param email 邮件
     * @return 验证码
     */
    boolean sendEmail(String email, String code);

    boolean checkEmail(String email);

    void register(UserVO user);

    String login(UserVO user);
}
