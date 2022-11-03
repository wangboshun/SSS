package com.zny.pipe.component.enums;

import com.zny.common.enums.DbTypeEnum;

import java.lang.annotation.*;

/**
 * @author WBS
 * Date 2022-10-30 9:24
 * SinkType
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SinkTypeEnum {
    DbTypeEnum value();
}
