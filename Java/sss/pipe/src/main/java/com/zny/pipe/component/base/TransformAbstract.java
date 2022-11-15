package com.zny.pipe.component.base;

import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.pipe.appication.*;
import com.zny.pipe.component.PipeStrategy;
import com.zny.pipe.component.enums.TaskStatusEnum;
import com.zny.pipe.component.transform.TransformUtils;
import com.zny.pipe.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date 2022-10-28 15:18
 * Transform抽象类
 */

@Component
public class TransformAbstract {

    public final TaskConfigApplication taskConfigApplication;

    public final SinkConfigApplication sinkConfigApplication;

    public final ConnectConfigApplication connectConfigApplication;

    public final FilterConfigApplication filterConfigApplication;

    public final ConvertConfigApplication convertConfigApplication;
    public final PipeStrategy pipeStrategy;
    public final RedisTemplate<String, String> redisTemplate;


    private final Logger logger = LoggerFactory.getLogger(getClass());
    private List<ConvertConfigModel> convertConfig;
    private List<FilterConfigModel> filterConfig;

    public TransformAbstract(TaskConfigApplication taskConfigApplication, SinkConfigApplication sinkConfigApplication, ConnectConfigApplication connectConfigApplication, FilterConfigApplication filterConfigApplication, ConvertConfigApplication convertConfigApplication, PipeStrategy pipeStrategy, RedisTemplate<String, String> redisTemplate) {
        this.taskConfigApplication = taskConfigApplication;
        this.sinkConfigApplication = sinkConfigApplication;
        this.connectConfigApplication = connectConfigApplication;
        this.filterConfigApplication = filterConfigApplication;
        this.convertConfigApplication = convertConfigApplication;
        this.pipeStrategy = pipeStrategy;
        this.redisTemplate = redisTemplate;
    }

    public void transform(MessageBodyModel body) {
        TaskStatusEnum status = TaskStatusEnum.values()[body.getStatus()];
        String taskId = body.getTaskId();
        TaskConfigModel taskConfig = taskConfigApplication.getById(taskId);
        filterConfig = filterConfigApplication.getFilterByTaskId(taskConfig.getId());
        convertConfig = convertConfigApplication.getConvertByTaskId(taskConfig.getId());
        List<Map<String, Object>> bodyData = body.getData();

        //1.过滤
        bodyData = this.filter(bodyData);

        //2.转换
        bodyData = this.convert(bodyData);

        SinkConfigModel sinkConfig = sinkConfigApplication.getById(taskConfig.getSink_id());
        ConnectConfigModel connectConfig = connectConfigApplication.getById(sinkConfig.getConnect_id());
        DbTypeEnum dbTypeEnum = DbTypeEnum.values()[connectConfig.getDb_type()];
        SinkBase sink = pipeStrategy.getSink(dbTypeEnum);
        sink.config(sinkConfig, connectConfig, taskConfig, body.getVersion());
        String cacheKey = RedisKeyEnum.SINK_TIME_CACHE + ":" + taskConfig.getId() + ":" + body.getVersion();
        Boolean hasKey = redisTemplate.hasKey(cacheKey);
        //如果缓存没有这个key，说明任务刚开始
        if (Boolean.FALSE.equals(hasKey)) {
            sink.setStatus(TaskStatusEnum.CREATE);
            sink.start(bodyData);
            sink.setStatus(TaskStatusEnum.RUNNING);
        } else {
            sink.start(bodyData);
        }
        if (status == TaskStatusEnum.COMPLETE) {
            sink.stop();
        }
    }

    /**
     * 过滤
     *
     * @param data 数据集
     */
    public List<Map<String, Object>> filter(List<Map<String, Object>> data) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : data) {
            if (TransformUtils.haveData(map, filterConfig)) {
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 转换
     *
     * @param data 数据集
     */
    public List<Map<String, Object>> convert(List<Map<String, Object>> data) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : data) {
            result.add(TransformUtils.updateData(map, convertConfig));
        }
        return result;
    }
}
