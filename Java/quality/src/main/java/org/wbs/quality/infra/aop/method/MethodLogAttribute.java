package org.wbs.quality.infra.aop.method;

import java.lang.annotation.*;

/**
 * @author WBS
 * Date:2022/6/6
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLogAttribute {
    String value() default "";
}
