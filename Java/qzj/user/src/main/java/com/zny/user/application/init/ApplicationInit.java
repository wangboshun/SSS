package com.zny.user.application.init;

import cn.dev33.satoken.util.SaResult;
import com.zny.user.application.ApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author WBS
 * Date:2022/9/9
 */

@Component
public class ApplicationInit {

    @Autowired
    private ApiApplication apiApplication;

    /**
     * 添加API接口信息
     */
    @PostConstruct
    public void addApi() {
        SaResult result = apiApplication.addApi();
    }
}
