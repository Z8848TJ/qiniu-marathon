package com.paper.sword.user.server;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paper.sword.common.auth.JwtTokenUtil;
import com.paper.sword.common.util.PaperSwordUtil;
import com.paper.sword.common.vo.UserVO;
import com.paper.sword.interfacer.user.AuthService;
import com.paper.sword.user.config.MailClient;
import com.paper.sword.user.entity.User;
import com.paper.sword.user.mapper.UserMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private MailClient mailCLient;
    
    @Override
    public boolean sendEmail(String email, String code) {
        
        String content = "欢迎使用搞子剑视频，您的验证码：" + code;
        
        return mailCLient.sendMail(email, "账号注册", content);
    }

    @Override
    public boolean checkEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);

        User user = userMapper.selectOne(wrapper);
        
        return user != null;
    }

    @Override
    public void register(UserVO userVO) {
        User user = new User();
        user.setUsername(userVO.getUsername());
        user.setEmail(userVO.getEmail());
        user.setSalt(PaperSwordUtil.generateUUID().substring(0, 5));
        user.setPassword(PaperSwordUtil.md5(user.getPassword() + user.getSalt()));
        user.setHeaderUrl("");
        user.setCreateTime(new Date());
        
        userMapper.insert(user);
    }

    @Override
    public String login(UserVO user) {
        // 验证账号
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        String username = user.getUsername();
        wrapper.eq(User::getUsername, username);
        User u = userMapper.selectOne(wrapper);
        
        if(u == null) {
            return null;
        }

        String password = PaperSwordUtil.md5(user.getPassword() + u.getSalt());
        
        if(!u.getPassword().equals(password)) {
            return null;
        }
        
        return JwtTokenUtil.createToken(u.getId());
    }
}
