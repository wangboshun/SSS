package com.zny.common.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WBS
 * Date:2022/9/1
 */

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi iotApi() {
        return GroupedOpenApi.builder().group("iot").pathsToMatch("/iot/**").build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("user").pathsToMatch("/user/**").build();
    }

    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder().group("system").pathsToMatch("/system/**").build();
    }
}
