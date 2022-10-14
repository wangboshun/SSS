package com.zny.system.application.api;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author WBS
 * Date:2022/9/26
 * api初始化
 */

@Component
public class ApiInit {
    private final ApiApplication apiApplication;

    public ApiInit(ApiApplication apiApplication) {
        this.apiApplication = apiApplication;
    }

    /**
     * 添加API接口信息
     */
    @PostConstruct
    public void addApi() {
        apiApplication.addApi();
    }
}
