package com.zny.host;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = "com.zny")
@ServletComponentScan
public class HostApplication {


    public static void main(String[] args) {

        SpringApplication.run(HostApplication.class, args);
    }

}
