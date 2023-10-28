package com.paper.sword.controller.user;

import com.paper.sword.common.util.RedisUtil;
import com.paper.sword.common.vo.Result;
import com.paper.sword.common.vo.UserVO;
import com.paper.sword.user.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

import static com.paper.sword.common.util.Constants.emailType.MODIFY;
import static com.paper.sword.common.util.Constants.emailType.REGISTER;

@RequestMapping("/auth")
@RestController
@Slf4j
@CrossOrigin
public class AuthController {
    
    @Reference
    AuthService authService;

    @Autowired
    private RedisTemplate<String, Object> template;
    
    
    @GetMapping("/sendEmail")
    public Result sendEmail(@RequestParam String email, @RequestParam int type) {
        log.info("发送验证码 ==> {}", email);

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
            template.opsForValue().set(codeKey, code, 15, TimeUnit.MINUTES);
            return Result.success().data("邮件发送成功");
        } 

        return Result.error().data("邮件发送失败，请联系管理员");
    }
    
    @PostMapping("/register")
    public Result register(@RequestBody UserVO user) {
        log.info("用户注册 ==> {}", user);
        // 空值处理
        if(user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        String email = user.getEmail();
        if (StringUtils.isBlank(email)) {
            return Result.error().data("邮箱不能为空");
        }

        String codeKey = RedisUtil.getRegisterKey(email);

        String code = (String)template.opsForValue().get(codeKey);
        
        if(code == null || !code.equals(user.getCode())) {
            return Result.error().data("验证码错误");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            return Result.error().data("密码不能为空");
        }
        
        
        authService.register(user);
        
        return Result.success().data("注册成功");
    }
    
    @PostMapping("/login")
    public Result login(@RequestBody UserVO user) {
        log.info("用户登录 ==> {}", user);
        // 空值处理
        String email = user.getEmail();
        if(StringUtils.isBlank(email)) {
            return Result.error().data("用户名不能为空");
        }

        String password = user.getPassword();
        if (StringUtils.isBlank(password)) {
            return Result.error().data("密码不能为空");
        }
        
        // 验证账号
        String token = authService.login(user);
        
        log.info("登录 token ==> {}", token);
        
        if(StringUtils.isBlank(token)) {
            return Result.error().data("登录失败");
        }
        
        return Result.success().data(token);
    }
    
}
