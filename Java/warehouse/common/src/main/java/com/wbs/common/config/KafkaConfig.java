package com.wbs.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author WBS
 * @date 2023/5/19 11:00
 * @desciption KafkaConfig 
 */
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@ConditionalOnProperty(value = "spring.kafka.enable") // 如果开启kafka,才进行配置
public class KafkaConfig extends KafkaAutoConfiguration {
    public KafkaConfig(KafkaProperties properties) {
        super(properties);
    }
}