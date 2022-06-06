package org.wbs.quality.attribute;

import java.lang.annotation.*;

/**
 * @author WBS
 * Date:2022/6/6
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodAopAttribute {
    String value() default "";
}
