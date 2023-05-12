package com.wbs.pipe.application.engine.base;

import cn.hutool.core.util.EnumUtil;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.pipe.application.SinkApplication;
import com.wbs.pipe.application.TaskApplication;
import com.wbs.pipe.model.event.EventAbstractModel;
import com.wbs.pipe.model.event.PipeEventModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import com.wbs.pipe.model.task.TaskInfoModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/4/26 11:25
 * @desciption PipeSubscriber
 */
@Component
public class PipeSubscriber {
    private final AsyncEventBus defaultEventBus;
    private final TaskApplication taskApplication;
    private final SinkApplication sinkApplication;

    private final RabbitTemplate rabbitTemplate;

    public PipeSubscriber(AsyncEventBus defaultEventBus, TaskApplication taskApplication, SinkApplication sinkApplication, RabbitTemplate rabbitmqTemplate, RabbitTemplate rabbitTemplate) {
        this.defaultEventBus = defaultEventBus;
        this.taskApplication = taskApplication;
        this.sinkApplication = sinkApplication;
        this.rabbitTemplate = rabbitTemplate;
        defaultEventBus.register(this);
    }

    @Subscribe
    public void receive(PipeEventModel event) {
        TaskInfoModel taskInfo = taskApplication.getTask(event.getTaskId(), null);
        String[] ids = taskInfo.getSink_id().split(",");
        String exchange = "Pipe_Exchange";
        for (String sinkId : ids) {
            SinkInfoModel sinkInfo = sinkApplication.getSink(sinkId, null);
            DbTypeEnum dbType = EnumUtil.getEnumMap(DbTypeEnum.class).get(sinkInfo.getType().toUpperCase());
            EventAbstractModel e = EngineManager.getEvent(dbType);
            if (e == null) {
                return;
            }
            e.setTaskInfo(taskInfo);
            e.setSinkInfo(sinkInfo);
            e.setDt(event.getDt());
            e.setBatchIndex(event.getBatchIndex());
            e.setBatchSize(event.getBatchSize());
            e.setEnd(event.isEnd());

            // eventbus发送消息
            // defaultEventBus.post(e);

            // rabbitmq发送消息
            String routingKey = dbType + "_RoutKey";
            rabbitTemplate.convertAndSend(exchange, routingKey, e);
        }
    }
}
