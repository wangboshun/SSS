package org.wbs.quality.spring.rabbitmq.queue;

import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wbs.quality.ApplicationMain;

import java.io.IOException;

/**
 * @author WBS
 * Date:2022/6/8
 */

@SpringBootTest(classes = ApplicationMain.class)
public class rabbitmq {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() {
        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(true);
        try {
            channel.queueDeclare("springboot-1", true, false, false, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rabbitTemplate.convertAndSend("springboot-1", "springboot msg   1111111111111111111111111111");
    }
}


