package com.zny.host;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@ComponentScan(basePackages = "com.zny.**")
@ServletComponentScan
public class HostApplication {

    public static void main(String[] args) {
        SpringApplication.run(HostApplication.class, args);
    }

}
