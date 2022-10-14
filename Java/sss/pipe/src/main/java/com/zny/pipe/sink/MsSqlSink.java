package com.zny.pipe.sink;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 */

@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MsSql_Queue", durable = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MsSql_RoutKey")})
public class MsSqlSink implements SinkBase {

    @RabbitHandler
    public void onMessage(String message) {
        System.out.println("MsSql_Queue --->接收消息:\r\n" + message);
    }

    @Override
    public void start() {
        try {


        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        try {

        } catch (Exception e) {

        }
    }
}
