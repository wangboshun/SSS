package com.zny.host;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.zny")
public class HostApplication {


    public static void main(String[] args) {

        SpringApplication.run(HostApplication.class, args);
    }

}
