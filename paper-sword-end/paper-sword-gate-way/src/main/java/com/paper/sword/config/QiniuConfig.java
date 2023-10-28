package com.paper.sword.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author: zzh
 * @description:
 */
@Configuration
@PropertySource("classpath:qiniu.properties")
@ConfigurationProperties(prefix = "qiniu")
@Data
@Slf4j
public class QiniuConfig {

    private String accessKey;

    private String secretKey;

    private String videoBucketName;

    private String videoBucketUrl;
    
    private String callbackUrl;
    
}
