package com.paper.sword.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: zzh
 * @description: 通用工具类
 */
public class PaperSwordUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    public static String md5(String key) {
        if(StringUtils.isBlank(key)) {
            return null;
        }

        return DigestUtils.md5DigestAsHex(key.getBytes());
    }


    /**
     * 将通知参数转化为字符串
     */
    public static String readDataFromHttp(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            for (String line; (line = br.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
