package com.zny.common.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WBS
 * Date:2022/9/5
 */

@Component
public class TopicAsyncEventBus implements IEventBus {
    private final Map<String, AsyncEventBus> eventBusMap;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    public TopicAsyncEventBus() {
        eventBusMap = new ConcurrentHashMap<>();
    }

    @Override
    public Registration register(String topic, Object listener) {
        AsyncEventBus eventBus = addEventBus(topic);
        eventBus.register(listener);
        return new Registration<Object>(topic, listener);
    }

    @Override
    public void unregister(Registration registration) {
        AsyncEventBus eventBus = addEventBus(registration.getTopic());
        eventBus.unregister(registration.getListener());
    }

    @Override
    public void unregister(String topic, Object listener) {
        AsyncEventBus eventBus = addEventBus(topic);
        eventBus.unregister(listener);
    }

    @Override
    public void post(String topic, Object event) {
        AsyncEventBus eventBus = addEventBus(topic);
        eventBus.post(event);
    }

    private AsyncEventBus addEventBus(String topic) {
        synchronized (eventBusMap) {
            AsyncEventBus eventBus = eventBusMap.get(topic);
            if (eventBus == null) {
                eventBus = new AsyncEventBus(executor);
                eventBusMap.put(topic, eventBus);
            }
            return eventBus;
        }
    }
}
