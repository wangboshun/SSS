package com.zny.common.eventbus.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 *
 */
public class LogExceptionHandler implements SubscriberExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(LogExceptionHandler.class);

    private static String message(SubscriberExceptionContext context) {
        Method method = context.getInvoke();
        return "Exception thrown by consumer method "
                + method.getName() + '(' + method.getParameterTypes()[0].getName() + ')'
                + " on consumer " + context.getConsumer()
                + " when dispatching event: " + context.getEvent();
    }

    /**
     * 事件订阅者异常处理器
     *
     * @param ex      异常
     * @param context 事件订阅处理上下文
     */
    public void handleException(Exception ex, SubscriberExceptionContext context) {
        log.error(message(context), ex);
    }
}
