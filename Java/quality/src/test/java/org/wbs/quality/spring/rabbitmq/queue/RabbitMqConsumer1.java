package org.wbs.quality.spring.rabbitmq.queue;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/*
 * 需要先创建这个队列 不然报错
 * */
@Component
@RabbitListener(queues = "springboot-1")
public class RabbitMqConsumer1 {

    @RabbitHandler
    public void receiveMsg(String msg) {
        System.out.println("receive msg: " + msg);
    }

}
