package com.wbs.pipe.application.engine.rabbitmq;

import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.Subscribe;
import com.wbs.common.enums.MQTypeEnum;
import com.wbs.common.extend.TopicAsyncEventBus;
import com.wbs.pipe.application.engine.base.IPipeSubscriber;
import com.wbs.pipe.application.engine.base.mq.MQSubscriberBase;
import com.wbs.pipe.model.event.MessageEventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/4/26 11:25
 * @desciption RabbitMQSubscriber
 */
@Component
public class RabbitMQSubscriber extends MQSubscriberBase implements IPipeSubscriber {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public RabbitMQSubscriber(TopicAsyncEventBus topicAsyncEventBus) {
        super();
        topicAsyncEventBus.register(MQTypeEnum.RABBITMQ + "_TOPIC", this);
    }

    /**
     * eventbus
     */
    @Subscribe
    @Override
    public void eventBusReceive(String message) {
        run(message);
    }

    /**
     * rabbitmq
     */
    @RabbitListener(errorHandler = "rabbitMessageErrorHandler", bindings = {@QueueBinding(value = @Queue(value = "RABBITMQ_QUEUE", durable = "false", autoDelete = "true"), exchange = @Exchange(value = "PIPE_EXCHANGE"), key = "RABBITMQ_ROUTKEY")})
    @Override
    public void rabbitMqReceive(String message) {
        run(message);
    }

    /**
     * kafka
     */
    @KafkaListener(topics = {"RABBITMQ_TOPIC"}, groupId = "PIPE_GROUP", errorHandler = "kafkaMessageErrorHandler")
    public void kafkaReceive(String message) {
        run(message);
    }

    @Override
    public void run(String message) {
        if (messageTimeout(message)) {
            return;
        }
        MessageEventModel model = JSONUtil.toBean(message, MessageEventModel.class);
        config(model.getTaskInfo(), model.getSinkInfo(), MQTypeEnum.RABBITMQ);
        process(model);
    }
}
