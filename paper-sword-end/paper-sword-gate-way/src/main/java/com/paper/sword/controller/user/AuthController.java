package com.paper.sword.controller.user;

import com.paper.sword.common.util.RedisUtil;
import com.paper.sword.common.vo.Result;
import com.paper.sword.common.vo.UserVO;
import com.paper.sword.user.AuthService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.paper.sword.common.util.Constants.emailType.MODIFY;
import static com.paper.sword.common.util.Constants.emailType.REGISTER;

@RequestMapping("/auth")
@RestController
public class AuthController {
    
    @Reference
    AuthService authService;

    @Autowired
    private RedisTemplate<String, Object> template;
    
    
    @PostMapping("/sendEmail/{type}")
    public Result sendEmail(@RequestBody String email, @PathVariable int type) {

        Map<String, Object> res = new HashMap<>();

        if (StringUtils.isBlank(email)) {
            return Result.error().data("邮箱不能为空");
        }

        // 验证邮箱
        boolean flag = authService.checkEmail(email);
        if (flag) {
            return Result.error().data("该邮箱已被注册");
        }

        String codeKey = "";

        if(type == REGISTER) {
            codeKey = RedisUtil.getRegisterKey(email);
        } else if(type == MODIFY) {
            codeKey = RedisUtil.getModifyKey(email);
        }

        if(!StringUtils.isBlank((String) template.opsForValue().get(codeKey))) {
            return Result.error().data("验证码未过期");
        }

        String code = RandomStringUtils.randomNumeric(6);
        
        if (authService.sendEmail(email, code)) {
            // 存入Redis
            template.opsForValue().set(codeKey, code, 5, TimeUnit.MINUTES);
            return Result.error().data("邮件发送成功");
        } 

        return Result.success().data("邮件发送失败，请联系管理员");
    }
    
    @PostMapping("/register")
    public Result register(@RequestBody UserVO user) {
        // 空值处理
        if(user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            return Result.error().data("邮箱不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return Result.error().data("密码不能为空");
        }
        
        authService.register(user);
        
        return Result.success();
    }
    
    @PostMapping("/login")
    public Result login(@RequestBody UserVO user) {
        // 空值处理
        String username = user.getUsername();
        if(StringUtils.isBlank(username)) {
            return Result.error().data("用户名不能为空");
        }

        String password = user.getPassword();
        if (StringUtils.isBlank(password)) {
            return Result.error().data("密码不能为空");
        }
        
        // 验证账号
        String token = authService.login(user);
        
        if(StringUtils.isBlank(token)) {
            return Result.error().data("登录失败");
        }
        
        return Result.success().data(token);
    }
    
}
