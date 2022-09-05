package com.zny.common.eventbus.core;

import java.lang.reflect.Method;

/**
 *
 */
public class SubscriberExceptionContext {

    private final EventBus bus;
    private final Object consumer;
    private final Method invoke;
    private final Event event;

    public SubscriberExceptionContext(EventBus bus, Object consumer, Method invoke, Event event) {
        this.bus = bus;
        this.consumer = consumer;
        this.invoke = invoke;
        this.event = event;
    }

    public EventBus getBus() {
        return bus;
    }

    public Object getConsumer() {
        return consumer;
    }

    public Method getInvoke() {
        return invoke;
    }

    public Event getEvent() {
        return event;
    }
}
