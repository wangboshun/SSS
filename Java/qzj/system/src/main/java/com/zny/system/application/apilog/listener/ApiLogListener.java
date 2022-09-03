package com.zny.system.application.apilog.listener;

import com.zny.common.event.ApiLogEvent;
import com.zny.common.utils.DateUtils;
import com.zny.system.application.apilog.ApiLogApplication;
import com.zny.system.model.apilog.ApiLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Component
public class ApiLogListener implements ApplicationListener<ApiLogEvent> {

    private final LinkedBlockingDeque<Map<String, Object>> queue = new LinkedBlockingDeque<>();
    @Autowired
    private ApiLogApplication apiLogApplication;

    @Async
    @Override
    public void onApplicationEvent(ApiLogEvent event) {
        queue.add(event.getMessage());
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
            ApiLogModel model = new ApiLogModel();
            model.setId(UUID.randomUUID().toString());
            BeanMap beanMap = BeanMap.create(model);
            beanMap.putAll(queue.remove());
            list.add(model);
        }
        boolean status = apiLogApplication.saveBatch(list, 500);
        System.out.printf("插入：" + list.size() + ":" + status);
    }
}
