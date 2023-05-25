package com.wbs.common.extend;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WBS
 * Date:2022/9/5
 * TopicAsyncEventBus
 */

@Component
public class TopicAsyncEventBus {
    private final Map<String, AsyncEventBus> eventBusMap;

    private final ThreadPoolTaskExecutor defaultExecutor;

    public TopicAsyncEventBus(ThreadPoolTaskExecutor defaultExecutor) {
        eventBusMap = new ConcurrentHashMap<>();
        this.defaultExecutor = defaultExecutor;
    }

    public void register(String topic, Object listener) {
        AsyncEventBus eventBus = addEventBus(topic);
        eventBus.register(listener);
    }

    public void unregister(String topic, Object listener) {
        AsyncEventBus eventBus = addEventBus(topic);
        eventBus.unregister(listener);
    }

    public void post(String topic, Object event) {
        AsyncEventBus eventBus = addEventBus(topic);
        eventBus.post(event);
    }

    private AsyncEventBus addEventBus(String topic) {
        synchronized (eventBusMap) {
            AsyncEventBus eventBus = eventBusMap.get(topic);
            if (eventBus == null) {
                eventBus = new AsyncEventBus(defaultExecutor);
                eventBusMap.put(topic, eventBus);
            }
            return eventBus;
        }
    }
}
