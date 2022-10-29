package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.SourceConfigModel;
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
 * SourceFactory
 */

@Component
public class SourceStrategy implements ApplicationContextAware {
    private final Map<String, SourceBase> sourceMap = new ConcurrentHashMap<>();

    public void run(TaskConfigModel taskConfig, SourceConfigModel sourceConfig, ConnectConfigModel connectConfig) {
        DbTypeEnum e = DbTypeEnum.values()[connectConfig.getDb_type()];
        SourceBase sink = sourceMap.get(e.toString());
        sink.config(sourceConfig, connectConfig, taskConfig);
        sink.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, SourceBase> beans = applicationContext.getBeansOfType(SourceBase.class);
        for (String key : beans.keySet()) {
            if ("sourceBase".equals(key) || "sourceAbstract".equals(key)) {
                continue;
            }
            sourceMap.put(beans.get(key).getName(), beans.get(key));
        }
    }
}
