package com.zny.system.application.apilog;

import com.zny.common.eventbus.core.Consumer;
import com.zny.common.eventbus.core.Listener;
import com.zny.common.eventbus.event.ApiLogEvent;
import com.zny.common.utils.DateUtils;
import com.zny.system.model.apilog.ApiLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/4
 */

@Consumer
public class ApiLogConsumer {

    @Autowired
    private ApiLogApplication apiLogApplication;

    /**
     * 监听日志事件
     *
     * @param event 事件
     */
    @Listener(topic = {"apilog"}, group = "log")
    public void addApiLog(ApiLogEvent event) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "消费日志任务：" + DateUtils.dateToStr(LocalDateTime.now()));
        List<Map<String, Object>> eventContent = event.getContent();
        List<ApiLogModel> list = new ArrayList<>();
        eventContent.forEach(x -> {
            ApiLogModel model = new ApiLogModel();
            BeanMap beanMap = BeanMap.create(model);
            beanMap.putAll(x);
            list.add(model);
        });

        boolean status = apiLogApplication.saveBatch(list, 500);
        System.out.println("apilog插入：" + list.size() + ":" + status);
    }
}
