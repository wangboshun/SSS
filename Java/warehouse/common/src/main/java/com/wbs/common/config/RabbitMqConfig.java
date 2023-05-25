package com.wbs.common.config;

import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author WBS
 * @date 2023/5/18 14:19
 * @desciption RabbitMqConfig
 */
@Configuration
@Import(RabbitAutoConfiguration.class)
@ConditionalOnProperty(value = "spring.rabbitmq.enable") // 如果开启rabbitmq,才进行配置
public class RabbitMqConfig  {
}
