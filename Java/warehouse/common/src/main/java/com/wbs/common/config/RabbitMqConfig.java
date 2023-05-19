package com.wbs.common.config;

import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author WBS
 * @date 2023/5/18 14:19
 * @desciption RabbitMqConfig
 */
@Configuration
@ConditionalOnProperty(value = "spring.rabbitmq.enable") // 如果开启rabbitmq,才进行配置
public class RabbitMqConfig extends RabbitAutoConfiguration {
}
