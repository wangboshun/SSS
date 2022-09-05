package com.zny.system.application.apilog;

import com.google.common.eventbus.Subscribe;
import com.zny.common.eventbus.EventEnum;
import com.zny.common.eventbus.IEventListener;
import com.zny.common.eventbus.TopicAsyncEventBus;
import com.zny.system.model.apilog.ApiLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/5
 */

@Component
public class ApiLogEventListener implements IEventListener<List<Map<String, Object>>> {

    @Autowired
    private ApiLogApplication apiLogApplication;

    @Autowired
    private TopicAsyncEventBus topicEventBus;

    @PostConstruct
    public void register() {
        topicEventBus.register(EventEnum.APILOG.toString(), this);
    }

    @Subscribe
    public void receive(List<Map<String, Object>> event) {
        List<ApiLogModel> list = new ArrayList<>();
        event.forEach(x -> {
            ApiLogModel model = new ApiLogModel();
            BeanMap beanMap = BeanMap.create(model);
            beanMap.putAll(x);
            list.add(model);
        });

        boolean status = apiLogApplication.saveBatch(list, 500);
        System.out.println("apilog插入：" + list.size() + ":" + status);
    }
}
