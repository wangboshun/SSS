package com.wbs.pipe.application.engine.clickhouse;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.pipe.application.ConnectApplication;
import com.wbs.pipe.application.engine.base.SubscriberAbstract;
import com.wbs.pipe.model.event.ClickHouseEventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/4/26 11:25
 * @desciption ClickHouseSubscriber
 */
@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "CLICKHOUSE_Queue", durable = "false", autoDelete = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "CLICKHOUSE_RoutKey")})
public class ClickHouseSubscriber extends SubscriberAbstract {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ClickHouseSubscriber(AsyncEventBus defaultEventBus, ConnectApplication connectApplication) {
        super(connectApplication);
        defaultEventBus.register(this);
    }

    /**
     * 监听消息
     *
     * @param message 数据消息
     */
    @Subscribe
    @RabbitHandler
    public void receive(ClickHouseEventModel message) {
        config(message.getTaskInfo(), message.getSinkInfo(), DbTypeEnum.CLICKHOUSE);
        process(message);
    }
}
