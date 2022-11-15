package com.zny.pipe.component.queue;

import com.zny.pipe.component.base.TransformAbstract;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 * mssql目的服务类
 */

@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MsSQL_Queue", durable = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MsSQL_RoutKey")})
public class MsSqlQueue extends QueueBase {

    public MsSqlQueue(TransformAbstract transformAbstract) {
        super(transformAbstract);
    }

    /**
     * 监听消息
     *
     * @param message 数据消息
     */
    @RabbitHandler
    public void onMessage(String message) {
        this.process(message);
    }
}
