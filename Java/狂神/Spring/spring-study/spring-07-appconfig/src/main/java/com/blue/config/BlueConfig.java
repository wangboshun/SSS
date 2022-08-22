package com.blue.config;

import com.blue.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.blue.pojo")
public class BlueConfig {

    @Bean
    public User getUser(){
        return new User();
    }
}
