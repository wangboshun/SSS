package com.zny.common.config;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author WBS
 * Date:2022/9/4
 */

@Configuration
public class AsyncEventBusConfig {

    @Bean
    public AsyncEventBus asyncEventBus() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5),
                new ThreadPoolExecutor.DiscardPolicy());
        //拒绝策略，抛弃任务

        return new AsyncEventBus(executor);
    }
}
