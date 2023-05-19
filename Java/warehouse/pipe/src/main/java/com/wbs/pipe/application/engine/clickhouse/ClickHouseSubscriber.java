package com.wbs.pipe.application.engine.clickhouse;

import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.Subscribe;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.extend.eventbus.TopicAsyncEventBus;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.application.engine.base.IPipeSubscriber;
import com.wbs.pipe.application.engine.base.SubscriberAbstract;
import com.wbs.pipe.model.event.MessageEventModel;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/4/26 11:25
 * @desciption ClickHouseSubscriber
 */
@Component
public class ClickHouseSubscriber extends SubscriberAbstract implements IPipeSubscriber {

    public ClickHouseSubscriber(TopicAsyncEventBus topicAsyncEventBus, ConnectApplication connectApplication) {
        super(connectApplication);
        topicAsyncEventBus.register(DbTypeEnum.CLICKHOUSE + "_TOPIC", this);
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
    @RabbitListener(errorHandler = "rabbitMessageErrorHandler", bindings = {@QueueBinding(value = @Queue(value = "CLICKHOUSE_QUEUE", durable = "false", autoDelete = "true"), exchange = @Exchange(value = "PIPE_EXCHANGE"), key = "CLICKHOUSE_ROUTKEY")})
    @Override
    public void rabbitMqReceive(String message) {
        run(message);
    }

    /**
     * kafka
     */
    @KafkaListener(topics = {"CLICKHOUSE_TOPIC"},groupId = "PIPE_GROUP", errorHandler = "kafkaMessageErrorHandler")
    public void kafkaReceive(String message) {
        run(message);
    }

    @Override
    public void run(String message) {
        MessageEventModel model = JSONUtil.toBean(message, MessageEventModel.class);
        config(model.getTaskInfo(), model.getSinkInfo(), DbTypeEnum.CLICKHOUSE);
        process(model);
    }
}
