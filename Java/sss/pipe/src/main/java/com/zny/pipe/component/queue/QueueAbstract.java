package com.zny.pipe.component.queue;

import com.google.gson.Gson;
import com.zny.common.enums.DbTypeEnum;
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
            boolean isStart = false;
            boolean isEnd = false;
            //如果当前条数批量大小或者小于批量大小，表示是刚开始
            if (body.getCurrent().equals(body.getBatch_size()) || body.getCurrent() < body.getBatch_size()) {
                isStart = true;
            }
            if (isStart) {
                sink.setStatus(TaskStatusEnum.CREATE, body.getVersion());
            }
            sink.config(sinkConfig, connectConfig, taskConfig);
            if (isStart) {
                sink.setStatus(TaskStatusEnum.RUNNING, body.getVersion());
            }
            sink.start(body.getData());
            //如果当前条数等于总条目，表示是结束
            if (body.getCurrent().equals(body.getTotal())) {
                isEnd = true;
            }
            if (isEnd) {
                sink.setStatus(TaskStatusEnum.COMPLETE, body.getVersion());
            }
        } catch (Exception e) {

        }
    }
}
