package com.example.springboot_1.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WBS
 * Date:2022/8/27
 */

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean registrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new ApiLogFilter());
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setName("ApiLogFilter");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }
}
