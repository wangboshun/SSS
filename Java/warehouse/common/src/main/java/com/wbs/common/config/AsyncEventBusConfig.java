package com.wbs.common.config;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author WBS
 * @date 2023/4/26 9:55
 * @desciption AsyncEventBusConfig
 */
@Configuration
public class AsyncEventBusConfig {

    private final ThreadPoolTaskExecutor defaultExecutor;

    public AsyncEventBusConfig(ThreadPoolTaskExecutor defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
    }

    @Bean(name = "defaultEventBus")
    public AsyncEventBus defaultEventBus() {
        return new AsyncEventBus(defaultExecutor);
    }
}
