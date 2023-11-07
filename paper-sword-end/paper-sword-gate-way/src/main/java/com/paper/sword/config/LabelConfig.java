package com.paper.sword.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author wwh
 * @date 2023/10/30
 */
@Configuration
@PropertySource("classpath:label.properties")
@ConfigurationProperties(prefix = "label")
@Data
@Slf4j
public class LabelConfig {

    public String scriptPath;

    public String outputDir;

}
