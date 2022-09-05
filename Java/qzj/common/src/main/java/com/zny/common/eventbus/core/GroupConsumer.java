package com.zny.common.eventbus.core;

import java.util.List;

/**
 *
 */
public class GroupConsumer {

    private String group;

    private List<EventConsumer> eventConsumers;

    public GroupConsumer() {
    }

    public GroupConsumer(String group, List<EventConsumer> eventConsumers) {
        this.group = group;
        this.eventConsumers = eventConsumers;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<EventConsumer> getEventConsumers() {
        return eventConsumers;
    }

    public void setEventConsumers(List<EventConsumer> eventConsumers) {
        this.eventConsumers = eventConsumers;
    }
}
