package com.paper.sword.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.paper.sword.common.vo.UserVO;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.common.auth.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("客户端请求接口 ==> {}, {}", request.getRequestURL(), request.getMethod());
        
        // 处理预检请求，直接放行
        if(request.getMethod().equals("OPTIONS")) {
            return true;
        }
        
        Map<String, Object> map = new HashMap<>();
        String token = request.getHeader("Authorization");
        
        if(token == null) {
            map.put("msg", "token为空");
        } else {
            log.info("验证 token ==> {}", token);
            try {
                DecodedJWT decodedJWT = JwtTokenUtil.verify(token);
                
                // 解析用户信息
                String userJSON = decodedJWT.getClaim("user").asString();
                log.info("用户信息 ==> {}", userJSON);
                UserVO user = JSON.parseObject(userJSON, UserVO.class);
                
                // 保存用户信息
                UserHolder.saveUser(user);
                return true;
            } catch (SignatureVerificationException e) {
                map.put("msg", "无效签名");
            } catch (TokenExpiredException e) {
                map.put("msg", "token过期");
            } catch (AlgorithmMismatchException e) {
                map.put("msg", "算法不匹配");
            } catch (InvalidClaimException e) {
                map.put("msg", "失效的payload");
            }
        }

        map.put("code", 501);
        
        String data = JSONObject.toJSONString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(data);
        
        log.error("token 验证失败：{}" , data);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户防止内存泄漏
        UserHolder.removeUser();
    }
}
