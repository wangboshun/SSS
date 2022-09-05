package com.zny.host;

import com.zny.common.eventbus.spring.EnableEventBus;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@ComponentScan(basePackages = "com.zny.**")
@ServletComponentScan
@MapperScan(basePackages = "com.zny.**")
@EnableEventBus
public class HostApplication {


    public static void main(String[] args) {

        SpringApplication.run(HostApplication.class, args);
    }

}
