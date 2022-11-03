package com.zny.pipe.component.sink;

import com.zny.common.enums.DbTypeEnum;
import com.zny.pipe.component.base.SinkAbstract;
import com.zny.pipe.component.enums.SinkTypeEnum;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 * mssql目的服务类
 */

@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MsSQL_Queue", durable = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MsSQL_RoutKey")})
@SinkTypeEnum(DbTypeEnum.MsSQL)
public class MsSqlSink extends SinkAbstract {

    @RabbitHandler
    public void onMessage(String message) {
        System.out.println("MsSqlSink --->接收消息:\r\n" + message);
        setData(message);
    }
}
