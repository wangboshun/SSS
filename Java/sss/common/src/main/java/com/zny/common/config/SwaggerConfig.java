package com.zny.common.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WBS
 * Date:2022/9/1
 * swagger配置类
 */

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("user").pathsToMatch("/user/**").build();
    }

    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder().group("system").pathsToMatch("/system/**").build();
    }

    @Bean
    public GroupedOpenApi pipeApi() {
        return GroupedOpenApi.builder().group("pipe").pathsToMatch("/pipe/**").build();
    }
}
