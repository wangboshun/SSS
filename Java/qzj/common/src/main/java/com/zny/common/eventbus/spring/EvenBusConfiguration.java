package com.zny.common.eventbus.spring;

import com.zny.common.eventbus.core.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class EvenBusConfiguration {

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    @Bean
    public EventBusBeanPostProcessor eventBusBeanPostProcessor() {
        return new EventBusBeanPostProcessor(eventBus());
    }
}
