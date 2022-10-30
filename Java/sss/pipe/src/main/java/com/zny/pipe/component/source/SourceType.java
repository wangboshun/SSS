package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;

import java.lang.annotation.*;

/**
 * @author WBS
 * Date 2022-10-30 9:26
 * SourceType
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SourceType {
    DbTypeEnum value();
}
