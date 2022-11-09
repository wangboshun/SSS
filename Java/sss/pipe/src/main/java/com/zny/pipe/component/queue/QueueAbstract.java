package com.zny.pipe.component.queue;

import com.google.gson.Gson;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.enums.RedisKeyEnum;
import com.zny.common.json.GsonEx;
import com.zny.pipe.appication.ConnectConfigApplication;
import com.zny.pipe.appication.SinkConfigApplication;
import com.zny.pipe.appication.TaskConfigApplication;
import com.zny.pipe.component.PipeStrategy;
import com.zny.pipe.component.base.SinkBase;
import com.zny.pipe.component.enums.TaskStatusEnum;
import com.zny.pipe.model.ConnectConfigModel;
import com.zny.pipe.model.MessageBodyModel;
import com.zny.pipe.model.SinkConfigModel;
import com.zny.pipe.model.TaskConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
    public PipeStrategy pipeStrategy;

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

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
            sink.config(sinkConfig, connectConfig, taskConfig, body.getVersion());
            String cacheKey = RedisKeyEnum.SINK_TIME_CACHE + ":" + taskConfig.getId() + ":" + body.getVersion();
            Boolean hasKey = redisTemplate.hasKey(cacheKey);
            //如果缓存没有这个key，缓存状态
            if (Boolean.FALSE.equals(hasKey)) {
                if (status == TaskStatusEnum.RUNNING) {
                    sink.setStatus(TaskStatusEnum.CREATE);
                }
                sink.start(body.getData());
                if (status == TaskStatusEnum.RUNNING) {
                    sink.setStatus(TaskStatusEnum.RUNNING);
                }
            }
            sink.start(body.getData());
            if (status == TaskStatusEnum.COMPLETE) {
                sink.stop();
            }
        } catch (Exception e) {

        }
    }
}
