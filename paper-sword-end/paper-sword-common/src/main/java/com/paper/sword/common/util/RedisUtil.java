package com.paper.sword.common.util;

/**
 * @author: zzh
 * @description: Redis 工具类
 */
public class RedisUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_REGISTER = "register";
    private static final String PREFIX_MODIFY = "modify";
    
    private static final String PREFIX_VIDEO = "video";

    public static String getRegisterKey(String email) {
        return PREFIX_REGISTER + SPLIT + email;
    }

    public static String getModifyKey(String email) {
        return PREFIX_MODIFY + SPLIT + email;
    }
    
    public static String getVideoScoreKey() {
        return PREFIX_VIDEO + SPLIT + "score";
    }
    
}
