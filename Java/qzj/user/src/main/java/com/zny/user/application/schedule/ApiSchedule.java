package com.zny.user.application.schedule;

import cn.dev33.satoken.util.SaResult;
import com.zny.user.application.ApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/9/9
 */

@Component
public class ApiSchedule {

    @Autowired
    private ApiApplication apiApplication;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void addApi(){
        SaResult result = apiApplication.addApi();
    }
}
