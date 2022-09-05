package com.zny.common.eventbus.core;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标记事件订阅者
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Consumer {
}
