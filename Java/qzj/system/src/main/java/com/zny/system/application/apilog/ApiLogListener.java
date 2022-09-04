package com.zny.system.application.apilog;

import com.google.common.eventbus.Subscribe;
import com.zny.common.eventbus.TopicEventBusImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author WBS
 * Date:2022/9/4
 */

@Component
public class ApiLogListener {

    @Autowired
    private TopicEventBusImpl topicEventBus;

    @PostConstruct
    public void register() {
        topicEventBus.register("ApiLogListener", this);
    }

    @Subscribe
    public void receive(String event) {
        System.out.println(event);
    }
}
