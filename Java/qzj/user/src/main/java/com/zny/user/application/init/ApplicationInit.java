package com.zny.user.application.init;

import com.zny.system.application.api.ApiApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author WBS
 * Date:2022/9/9
 */

@Component
public class ApplicationInit {

    private final ApiApplication apiApplication;

    public ApplicationInit(ApiApplication apiApplication) {
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
