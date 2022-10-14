package com.zny.pipe.sink;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/10/12
 */

@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MySql_Queue", durable = "true"),
        exchange = @Exchange(value = "Pipe_Exchange"), key = "MySql_RoutKey")})
public class MySqlSink implements SinkBase {

    @RabbitHandler
    public void onMessage(String message) {
        System.out.println("MySql_Queue --->接收消息:\r\n" + message);
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
