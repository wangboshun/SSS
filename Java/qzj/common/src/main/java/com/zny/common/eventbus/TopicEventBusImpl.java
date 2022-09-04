package com.zny.common.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author WBS
 * Date:2022/9/4
 */

@Component
public class TopicEventBusImpl implements TopicEventBus {
    private final Map<String, AsyncEventBus> eventBusMap;
    private final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(3, 5, 60, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(5), new ThreadPoolExecutor.DiscardPolicy());

    public TopicEventBusImpl() {
        eventBusMap = new ConcurrentHashMap<>();
    }

    @Override
    public Registration register(String topic, Object listener) {
        EventBus eventBus = addEventBus(topic);
        eventBus.register(listener);
        return new Registration<Object>(topic, listener);
    }

    @Override
    public void unregister(Registration registration) {
        EventBus eventBus = addEventBus(registration.getTopic());
        eventBus.unregister(registration.getListener());
    }

    @Override
    public void unregister(String topic, Object listener) {
        EventBus eventBus = addEventBus(topic);
        eventBus.unregister(listener);
    }

    @Override
    public void post(String topic, Object event) {
        EventBus eventBus = addEventBus(topic);
        eventBus.post(event);
    }

    private EventBus addEventBus(String topic) {
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
