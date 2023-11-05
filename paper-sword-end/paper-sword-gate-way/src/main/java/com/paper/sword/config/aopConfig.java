package com.paper.sword.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wwh
 * @date 2023/11/4
 */
@Import({
        // 切面实现类路径
        com.paper.sword.aspect.ControlsLogAspect.class
})
@Configuration
public class aopConfig {
}
