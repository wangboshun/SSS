package com.wbs.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author WBS
 * @date 2023/5/19 11:00
 * @desciption KafkaConfig
 */
@Configuration
@Import(KafkaAutoConfiguration.class)
@ConditionalOnProperty(value = "spring.kafka.enable") // 如果开启kafka,才进行配置
public class KafkaConfig {
}