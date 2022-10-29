package com.zny.pipe.component.sink;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SinkConfigModel;
import com.zny.pipe.model.TaskConfigModel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WBS
 * Date 2022-10-29 16:36
 * SinkFactory
 */

@Component
public class SinkStrategy implements ApplicationContextAware {
    private final Map<String, SinkBase> sinkMap = new ConcurrentHashMap<>();

    public void run(TaskConfigModel taskConfig, SinkConfigModel sinkConfig, ConnectConfigModel connectConfig) {
        DbTypeEnum e = DbTypeEnum.values()[connectConfig.getDb_type()];
        SinkBase sink = sinkMap.get(e.toString());
        sink.config(sinkConfig, connectConfig, taskConfig);
        sink.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, SinkBase> beans = applicationContext.getBeansOfType(SinkBase.class);
        for (String key : beans.keySet()) {
            if ("sinkBase".equals(key) || "sinkAbstract".equals(key)) {
                continue;
            }
            sinkMap.put(beans.get(key).getName(), beans.get(key));
        }
    }
}
