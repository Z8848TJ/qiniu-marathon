package com.paper.sword.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: zzh
 * @description:
 */
@Configuration
public class CrossConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE")
                    .allowCredentials(false)
                    .maxAge(3600)
                    .allowedHeaders("Authorization", "Content-Type");
    }
    
}
