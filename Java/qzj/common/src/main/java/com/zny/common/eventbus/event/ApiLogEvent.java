package com.zny.common.eventbus.event;

import com.zny.common.eventbus.core.Event;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/5
 */

public class ApiLogEvent implements Event {

    private String topic;
    private List<Map<String, Object>> content;

    @Override
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public List<Map<String, Object>> getContent() {
        return content;
    }

    public void setContent(List<Map<String, Object>> content) {
        this.content = content;
    }
}
