package com.zny.pipe.component;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SinkBase;
import com.zny.pipe.component.base.SourceBase;
import com.zny.pipe.component.enums.SinkTypeEnum;
import com.zny.pipe.component.enums.SourceTypeEnum;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WBS
 * Date 2022-10-29 16:36
 * PipeStrategy
 */

@Component
public class PipeStrategy implements ApplicationContextAware {
    private final Map<String, SourceBase> sourceMap = new ConcurrentHashMap<>();
    private final Map<String, SinkBase> sinkMap = new ConcurrentHashMap<>();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> sourceBeans = applicationContext.getBeansWithAnnotation(SourceTypeEnum.class);
        for (Object bean : sourceBeans.values()) {
            Class<SourceBase> entity = (Class<SourceBase>) bean.getClass();
            DbTypeEnum e = entity.getAnnotation(SourceTypeEnum.class).value();
            sourceMap.put(e.toString(), applicationContext.getBean(entity));
        }

        Map<String, Object> sinkBeans = applicationContext.getBeansWithAnnotation(SinkTypeEnum.class);
        for (Object bean : sinkBeans.values()) {
            Class<SinkBase> entity = (Class<SinkBase>) bean.getClass();
            DbTypeEnum e = entity.getAnnotation(SinkTypeEnum.class).value();
            sinkMap.put(e.toString(), applicationContext.getBean(entity));
        }
    }

    /**
     * 获取目的源
     *
     * @param e 数据类型
     */
    public SinkBase getSink(DbTypeEnum e) {
        return sinkMap.get(e.toString());
    }

    /**
     * 获取数据源
     *
     * @param e 数据类型
     */
    public SourceBase getSource(DbTypeEnum e) {
        return sourceMap.get(e.toString());
    }
}
