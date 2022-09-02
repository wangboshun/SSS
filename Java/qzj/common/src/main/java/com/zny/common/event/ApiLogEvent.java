package com.zny.common.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/2
 */

public class ApiLogEvent extends ApplicationEvent {

    private Map<String, Object> message;

    public ApiLogEvent(Map<String, Object> message) {
        super(message);
        this.message = message;
    }

    public Map<String, Object> getMessage() {
        return message;
    }
}
