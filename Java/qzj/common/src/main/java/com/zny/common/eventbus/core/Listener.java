package com.zny.common.eventbus.core;

import java.lang.annotation.*;

/**
 * 事件订阅者方法监听
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Listener {

    /**
     * 监听事件主题
     *
     * @return
     */
    String[] topic() default {};

    /**
     * 监听事件所属分组
     *
     * @return
     */
    String group() default "";
}
