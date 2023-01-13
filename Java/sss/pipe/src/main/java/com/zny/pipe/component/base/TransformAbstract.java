package com.zny.pipe.component.base;

import com.google.gson.Gson;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.common.json.GsonEx;
import com.zny.pipe.appication.*;
import com.zny.pipe.component.PipeStrategy;
import com.zny.pipe.component.base.enums.TaskStatusEnum;
import com.zny.pipe.component.base.interfaces.SinkBase;
import com.zny.pipe.component.transform.TransformUtils;
import com.zny.pipe.model.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date 2022-10-28 15:18
 * Transform抽象类
 * 1.过滤
 * 2.替换
 * 3.映射
 */

@Component
public class TransformAbstract {

    public final TaskConfigApplication taskConfigApplication;
    public final SinkConfigApplication sinkConfigApplication;
    public final ConnectConfigApplication connectConfigApplication;
    public final FilterConfigApplication filterConfigApplication;
    public final ConvertConfigApplication convertConfigApplication;
    public final ColumnConfigApplication columnConfigApplication;
    public final PipeStrategy pipeStrategy;
    public final RedisTemplate<String, String> redisTemplate;
    private List<ConvertConfigModel> convertConfig;
    private List<FilterConfigModel> filterConfig;
    private List<ColumnConfigModel> columnList;
    private String CACHE_KEY;

    public TransformAbstract(TaskConfigApplication taskConfigApplication, SinkConfigApplication sinkConfigApplication, ConnectConfigApplication connectConfigApplication, FilterConfigApplication filterConfigApplication, ConvertConfigApplication convertConfigApplication, ColumnConfigApplication columnConfigApplication, PipeStrategy pipeStrategy, RedisTemplate<String, String> redisTemplate) {
        this.taskConfigApplication = taskConfigApplication;
        this.sinkConfigApplication = sinkConfigApplication;
        this.connectConfigApplication = connectConfigApplication;
        this.filterConfigApplication = filterConfigApplication;
        this.convertConfigApplication = convertConfigApplication;
        this.columnConfigApplication = columnConfigApplication;
        this.pipeStrategy = pipeStrategy;
        this.redisTemplate = redisTemplate;
    }

    public void transform(MessageBodyModel body) {
        TaskStatusEnum status = TaskStatusEnum.values()[body.getStatus()];
        String taskId = body.getTaskId();
        TaskConfigModel taskConfig = taskConfigApplication.getById(taskId);
        CACHE_KEY = RedisKeyEnum.SINK_TIME_CACHE + ":" + taskConfig.getId() + ":" + body.getVersion();
        filterConfig = filterConfigApplication.getFilterByTaskId(taskConfig.getId());
        convertConfig = convertConfigApplication.getConvertByTaskId(taskConfig.getId());
        columnList = columnConfigApplication.getColumnByTaskId(taskConfig.getId());
        List<Map<String, Object>> bodyData = body.getData();
        Gson gson = GsonEx.getInstance();

        //1.过滤
        bodyData = this.filter(bodyData);

        //2.替换
        bodyData = this.convert(bodyData);

        //3.映射
        bodyData = this.mapper(bodyData);

        SinkConfigModel sinkConfig = sinkConfigApplication.getById(taskConfig.getSink_id());
        ConnectConfigModel connectConfig = connectConfigApplication.getById(sinkConfig.getConnect_id());
        DbTypeEnum dbTypeEnum = DbTypeEnum.values()[connectConfig.getDb_type()];
        SinkBase sink = pipeStrategy.getSink(dbTypeEnum);
        sink.config(sinkConfig, connectConfig, taskConfig, columnList, body.getVersion());
        Boolean hasKey = redisTemplate.hasKey(CACHE_KEY);
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
        if (filterConfig == null || filterConfig.isEmpty()) {
            return data;
        }
        int count = 0;
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : data) {
            if (TransformUtils.haveData(map, filterConfig)) {
                result.add(map);
            } else {
                count++;
            }
        }

        redisTemplate.opsForHash().put(CACHE_KEY.replace("SINK", "SOURCE"), "FILTER_COUNT", count + "");
        return result;
    }

    /**
     * 转换
     *
     * @param data 数据集
     */
    public List<Map<String, Object>> convert(List<Map<String, Object>> data) {
        if (convertConfig == null || convertConfig.isEmpty()) {
            return data;
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : data) {
            result.add(TransformUtils.convertData(map, convertConfig));
        }
        return result;
    }

    /**
     * 映射
     *
     * @param data 数据集
     */
    public List<Map<String, Object>> mapper(List<Map<String, Object>> data) {
        if (columnList == null || columnList.isEmpty()) {
            return data;
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : data) {
            result.add(TransformUtils.mapperData(map, columnList));
        }
        return result;
    }

}
