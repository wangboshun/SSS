package com.zny.pipe.component.base.annotations;

import com.zny.common.enums.DbTypeEnum;

import java.lang.annotation.*;

/**
 * @author WBS
 * Date 2022-10-30 9:24
 * Sink注解
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SinkTypeAnnotation {
    DbTypeEnum value();
}
