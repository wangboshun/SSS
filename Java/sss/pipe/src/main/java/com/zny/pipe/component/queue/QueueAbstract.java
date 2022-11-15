package com.zny.pipe.component.queue;

import com.google.gson.Gson;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.common.json.GsonEx;
import com.zny.pipe.appication.*;
import com.zny.pipe.component.PipeStrategy;
import com.zny.pipe.component.base.SinkBase;
import com.zny.pipe.component.enums.TaskStatusEnum;
import com.zny.pipe.component.filter.CompareFilter;
import com.zny.pipe.component.filter.FilterBase;
import com.zny.pipe.component.transform.TransformAbstract;
import com.zny.pipe.component.transform.TransformBase;
import com.zny.pipe.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WBS
 * Date 2022-11-05 13:41
 * 消息处理基类
 */

@Component
public class QueueAbstract {

    @Autowired
    public TaskConfigApplication taskConfigApplication;

    @Autowired
    public SinkConfigApplication sinkConfigApplication;

    @Autowired
    public ConnectConfigApplication connectConfigApplication;

    @Autowired
    private FilterConfigApplication filterConfigApplication;

    @Autowired
    private ConvertConfigApplication convertConfigApplication;

    @Autowired
    public PipeStrategy pipeStrategy;

    @Autowired
    public RedisTemplate<String, String> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 数据处理
     *
     * @param message 数据消息
     */
    public void process(String message) {
        try {
            Gson gson = GsonEx.getInstance();
            MessageBodyModel body = gson.fromJson(message, MessageBodyModel.class);
            String taskId = body.getTaskId();
            TaskConfigModel taskConfig = taskConfigApplication.getById(taskId);
            SinkConfigModel sinkConfig = sinkConfigApplication.getById(taskConfig.getSink_id());
            ConnectConfigModel connectConfig = connectConfigApplication.getById(sinkConfig.getConnect_id());
            DbTypeEnum dbTypeEnum = DbTypeEnum.values()[connectConfig.getDb_type()];
            SinkBase sink = pipeStrategy.getSink(dbTypeEnum);
            TaskStatusEnum status = TaskStatusEnum.values()[body.getStatus()];

            //数值筛选配置
            List<FilterConfigModel> filterConfig = filterConfigApplication.getFilterByTaskId(taskConfig.getId());
            FilterBase filter = new CompareFilter();
            //传递过滤参数配置
            filter.config(filterConfig.stream().filter(x -> x.getUse_type() == 0).collect(Collectors.toList()));

            //数值转换配置
            List<ConvertConfigModel> convertConfig = convertConfigApplication.getConvertByTaskId(taskConfig.getId());
            TransformBase transform = new TransformAbstract();
            //传递转换参数和过滤参数配置
            transform.config(convertConfig);

            //基础配置
            sink.config(sinkConfig, connectConfig, taskConfig, body.getVersion());

            //2.过滤配置
            sink.filter(filter);

            //3.转换配置
            sink.transform(transform);

            String cacheKey = RedisKeyEnum.SINK_TIME_CACHE + ":" + taskConfig.getId() + ":" + body.getVersion();
            Boolean hasKey = redisTemplate.hasKey(cacheKey);
            //如果缓存没有这个key，说明任务刚开始
            if (Boolean.FALSE.equals(hasKey)) {
                sink.setStatus(TaskStatusEnum.CREATE);
                sink.start(body.getData());
                sink.setStatus(TaskStatusEnum.RUNNING);
            } else {
                sink.start(body.getData());
            }
            if (status == TaskStatusEnum.COMPLETE) {
                sink.stop();
            }
        } catch (Exception e) {
            logger.error("QueueAbstract process", e);
            System.out.println("QueueAbstract process: " + e.getMessage());
        }
    }
}
