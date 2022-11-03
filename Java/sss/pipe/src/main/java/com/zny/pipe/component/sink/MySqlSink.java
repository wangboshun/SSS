package com.zny.pipe.component.sink;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SinkAbstract;
import com.zny.pipe.component.enums.SinkTypeEnum;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 * mysql目的服务类
 */

@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MySQL_Queue", durable = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MySQL_RoutKey")})
@SinkTypeEnum(DbTypeEnum.MySQL)
public class MySqlSink extends SinkAbstract {

    @RabbitHandler
    public void onMessage(String message) {
        System.out.println("MySqlSink --->接收消息:\r\n" + message);
        setData(message);
    }
}
