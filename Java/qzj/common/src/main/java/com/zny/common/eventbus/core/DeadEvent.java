package com.zny.common.eventbus.core;

/**
 *
 */
public class DeadEvent {

    private Object source;
    private Event event;

    public DeadEvent() {
    }

    public DeadEvent(Object source, Event event) {
        this.source = source;
        this.event = event;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
