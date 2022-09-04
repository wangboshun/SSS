package com.zny.system.application.apilog;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/4
 * 日志信息监听类
 */

@Service
public class ApiLogListener {
    @Autowired
    private AsyncEventBus asyncEventBus;

    @PostConstruct
    public void register() {
        asyncEventBus.register(this);
    }

    @AllowConcurrentEvents
    @Subscribe
    public void receive(Map<String, Object> event) {
        try {
            System.out.println("onMessageEvent---》" + Thread.currentThread().getName() + ":" + event);
        } catch (Exception e) {

        }
    }
}
