package com.paper.sword.common.annotation;

import java.lang.annotation.*;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControlsLog {
    int operateType() default 0;
}
