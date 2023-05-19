package com.wbs.pipe.application.engine.base;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.extend.eventbus.TopicAsyncEventBus;
import com.wbs.pipe.application.SinkApplication;
import com.wbs.pipe.application.TaskApplication;
import com.wbs.pipe.model.event.MessageEventModel;
import com.wbs.pipe.model.event.PipeEventModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import com.wbs.pipe.model.task.TaskInfoModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/4/26 11:25
 * @desciption PipeSubscriber
 */
@Component
public class PipeSubscriber {
    private final TopicAsyncEventBus topicEventBus;
    private final TaskApplication taskApplication;
    private final SinkApplication sinkApplication;
    private final RabbitTemplate rabbitTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String senType;

    public PipeSubscriber(@Value("${pipe.send-type}") String sendType, @Value("${spring.rabbitmq.enable}") boolean openRabbitMq, @Value("${spring.kafka.enable}") boolean openKafka, AsyncEventBus defaultEventBus, TopicAsyncEventBus topicEventBus, TaskApplication taskApplication, SinkApplication sinkApplication) {
        this.senType = sendType;
        this.topicEventBus = topicEventBus;
        this.taskApplication = taskApplication;
        this.sinkApplication = sinkApplication;
        if (openRabbitMq && senType.equals("rabbitmq")) {
            this.rabbitTemplate = SpringUtil.getBean("rabbitTemplate");
        } else {
            this.rabbitTemplate = null;
        }

        if (openKafka && senType.equals("kafka")) {
            this.kafkaTemplate = SpringUtil.getBean("kafkaTemplate");
        } else {
            this.kafkaTemplate = null;
        }

        defaultEventBus.register(this);
    }

    @Subscribe
    public void receive(PipeEventModel event) {
        TaskInfoModel taskInfo = taskApplication.getTask(event.getTaskId(), null);
        String[] ids = taskInfo.getSink_id().split(",");
        for (String sinkId : ids) {
            SinkInfoModel sinkInfo = sinkApplication.getSink(sinkId, null);
            DbTypeEnum dbType = EnumUtil.getEnumMap(DbTypeEnum.class).get(sinkInfo.getType().toUpperCase());
            MessageEventModel e = new MessageEventModel();
            e.setTaskInfo(taskInfo);
            e.setSinkInfo(sinkInfo);
            e.setTable(event.getTable());
            e.setBatchIndex(event.getBatchIndex());
            e.setBatchSize(event.getBatchSize());
            e.setEnd(event.isEnd());

            JSONConfig jsonConfig = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONObject object = new JSONObject(e, jsonConfig);
            String message = JSONUtil.toJsonStr(object);

            // eventbus发送消息
            if (senType.equals("memory")) {
                topicEventBus.post(dbType + "_TOPIC", message);
            }
            // rabbitmq发送消息
            else if (senType.equals("rabbitmq")) {
                String routingKey = dbType + "_ROUTKEY";
                rabbitTemplate.convertAndSend("PIPE_EXCHANGE", routingKey, message);
            }
            // kafka发送消息
            else if (senType.equals("kafka")) {
                kafkaTemplate.send(dbType + "_TOPIC", message);
            }
        }
    }
}
