package com.zny.system.application.apilog.listener;

import com.zny.common.event.ApiLogEvent;
import com.zny.common.utils.DateUtils;
import com.zny.system.application.apilog.ApiLogApplication;
import com.zny.system.model.apilog.ApiLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Component
public class ApiLogListener implements ApplicationListener<ApiLogEvent> {

    private final LinkedBlockingDeque<ApiLogModel> queue = new LinkedBlockingDeque<ApiLogModel>();
    @Autowired
    private ApiLogApplication apiLogApplication;

    @Override
    public void onApplicationEvent(ApiLogEvent event) {
        ApiLogModel model = new ApiLogModel();
        model.id = UUID.randomUUID().toString();
        BeanMap beanMap = BeanMap.create(model);
        beanMap.putAll(event.getMessage());
        queue.add(model);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void addApiLog() {
        System.out.println("插入日志任务：" + DateUtils.dateToStr(LocalDateTime.now()));
        int size = queue.size();
        if (size < 1) {
            return;
        }
        List<ApiLogModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(queue.remove());
        }
        boolean status = apiLogApplication.saveBatch(list, 500);
        System.out.printf("插入：" + list.size() + ":" + status);
    }
}
