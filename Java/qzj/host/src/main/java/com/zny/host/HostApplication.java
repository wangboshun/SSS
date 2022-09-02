package com.zny.host;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@ComponentScan(basePackages = "com.zny.**")
@ServletComponentScan
@EnableScheduling
@MapperScan(basePackages = "com.zny.**")
public class HostApplication {


    public static void main(String[] args) {

        SpringApplication.run(HostApplication.class, args);
    }

}
