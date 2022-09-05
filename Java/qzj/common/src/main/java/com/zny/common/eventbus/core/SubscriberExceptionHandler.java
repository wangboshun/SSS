package com.zny.common.eventbus.core;

/**
 *
 */
public interface SubscriberExceptionHandler {

    /**
     * 事件订阅者异常处理器
     *
     * @param ex      异常
     * @param context 事件订阅处理上下文
     */
    void handleException(Exception ex, SubscriberExceptionContext context);
}
