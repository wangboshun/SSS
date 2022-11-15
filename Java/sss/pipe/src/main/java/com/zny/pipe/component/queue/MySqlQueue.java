package com.zny.pipe.component.queue;

import com.zny.pipe.component.base.TransformAbstract;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 * mysql目的服务类
 */

/**
 * MySql消息接受者
 * 默认不持久化消息
 */
@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MySQL_Queue", durable = "false"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MySQL_RoutKey")})
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
