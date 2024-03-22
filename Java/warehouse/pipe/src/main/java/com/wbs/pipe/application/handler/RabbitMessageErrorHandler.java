package com.wbs.pipe.application.handler;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/5/19 15:40
 * @desciption RabbitErrorHandler
 */
@Component
public class RabbitMessageErrorHandler implements RabbitListenerErrorHandler {
    @Override
    public Object handleError(Message message, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) throws Exception {
        return null;
    }
}
