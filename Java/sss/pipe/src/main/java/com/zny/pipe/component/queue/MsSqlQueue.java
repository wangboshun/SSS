package com.zny.pipe.component.queue;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 * mssql目的服务类
 */

@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MsSQL_Queue", durable = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MsSQL_RoutKey")})
public class MsSqlQueue extends QueueAbstract {

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
