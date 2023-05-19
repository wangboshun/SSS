package com.wbs.pipe.application.handler;

import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/5/19 15:30
 * @desciption NoRetryErrorHandler
 */
@Component
public class KafkaMessageErrorHandler implements KafkaListenerErrorHandler {


    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        return null;
    }
}
