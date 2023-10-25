package com.paper.sword.common.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;

public class JwtTokenUtil {

    private static final String USER = "user";
    
    private static final String SIGN = "taafqU4xVVTd5T1q";
    
    // 根据用户信息生成 Token
    public static String createToken(String msg) {

        Calendar instance = Calendar.getInstance();
        // instance.add(Calendar.DATE, 30);
        instance.add(Calendar.MINUTE, 60);
        
        return JWT.create()
                .withClaim(USER, msg)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGN));
    }
    
    // 验证token
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
}
