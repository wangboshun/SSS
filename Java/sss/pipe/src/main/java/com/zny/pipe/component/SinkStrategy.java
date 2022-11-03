package com.zny.pipe.component;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SinkBase;
import com.zny.pipe.component.enums.SinkTypeEnum;
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
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(SinkTypeEnum.class);
        for (Object bean : beans.values()) {
            Class<SinkBase> entity = (Class<SinkBase>) bean.getClass();
            DbTypeEnum e = entity.getAnnotation(SinkTypeEnum.class).value();
            sinkMap.put(e.toString(), applicationContext.getBean(entity));
        }
    }
}
