package com.paper.sword.common.annotation;

import com.paper.sword.common.enumType.OperateType;

import java.lang.annotation.*;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControlsLog {
    OperateType operateType() default OperateType.like;
}
