package com.zny.pipe.component.queue;

import com.zny.pipe.component.base.TransformAbstract;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * MySql消息接受者
 * 默认不持久化消息
 * 所有连接断开后,自动删除队列
 */
@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MySql_Queue", durable = "false", autoDelete = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MySql_RoutKey")})
public class MySqlQueue extends QueueBase {

    public MySqlQueue(TransformAbstract transformAbstract) {
        super(transformAbstract);
    }

    /**
     * 监听消息
     *
     * @param message 数据消息
     */
    @RabbitHandler
    public void onMessage(String message) {
        process(message);
    }
}
