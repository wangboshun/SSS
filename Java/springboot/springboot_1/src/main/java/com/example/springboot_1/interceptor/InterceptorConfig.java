package com.example.springboot_1.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author WBS
 * Date:2022/8/27
 */

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ApiLogInterceptor interceptor = new ApiLogInterceptor();
        registry.addInterceptor(interceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}
