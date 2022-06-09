package org.wbs.quality.infra.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author WBS
 * Date:2022/6/8
 * <p>
 * <p>
 * 创建路由： channel.exchangeDeclare(exchange,"fanout");
 * exchange 路由名称
 * type 路由类型，这⾥可以⽤string类型，我直接⽤了架包⾥⾃带的枚举类
 * durable true：持久化操作，rabbitmq重启后，依然存在
 * autoDelete true：⾃动删除，当没被使⽤时，⾃动删除该路由
 * internal
 * arguments 创建路由时的构造参数
 * channel.exchangeDeclare(exchange,"fanout");
 * <p>
 * <p>
 * 创建队列：  channel.queueDeclare(queue, true, false, false, null);
 * queue 队列名称
 * durable 是否申明持久队列，true：rabbitmq重启后继续存在
 * exclusive 是否独占，ture：只有该连接能访问
 * autoDelete 是否⾃动删除，true：服务器不再使⽤时，将⾃动删除该队列
 * arguments 队列其它参数
 * <p>
 * <p>
 * 队列绑定路由：  channel.queueBind(queue, exchange, "");
 * queue 队列名称
 * exchange 路由名称
 * routingKey 路由密钥
 * <p>
 * <p>
 * 推送消息： channel.basicPublish(exchange, routingKey, null, msg.getBytes());
 * exchange 路由
 * routingKey 路由密钥
 * props 消息的头信息
 * body 消息主体
 */

@Component
public class RabbitMqUtils {

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private Integer port;

    @Value("${rabbitmq.vh}")
    private String virtualHost;

    @Value("${rabbitmq.username}")
    private String username;

    @Value("${rabbitmq.password}")
    private String password;

    private Connection connection;

    /**
     * @return rabbitmq连接
     * @throws IOException
     * @throws TimeoutException
     */
    private Connection getConnection() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setVirtualHost(virtualHost);
            factory.setUsername(username);
            factory.setPassword(password);
            connection = factory.newConnection();
            return connection;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Work模式发送
     */
    public void sendMsgForWork(String queue, String msg) {
        try {
            if (null == connection) {
                getConnection();
            }
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue, true, false, false, null);
            channel.basicPublish("", queue, null, msg.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Work模式接收
     */
    public void receiveMsgForWork(String queue) {

        try {
            if (null == connection) {
                getConnection();
            }
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue, true, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received '" + message + "'");
            };
            channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Topic模式发送
     */
    public void sendMsgForTopic(String exchange, String routingKey, String msg) {
        try {
            if (null == connection) {
                getConnection();
            }

            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, "topic");
            channel.basicPublish(exchange, routingKey, null, msg.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Topic模式接收
     */
    public void receiveMsgForTopic(String exchange, String[] routingKey) {

        try {
            if (null == connection) {
                getConnection();
            }
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, "topic");
            String queueName = exchange + "_topic_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            for (String bindingKey : routingKey) {
                channel.queueBind(queueName, exchange, bindingKey);
            }

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Direct模式发送
     */
    public void sendMsgForDirect(String exchange, String routingKey, String msg) {
        try {
            if (null == connection) {
                getConnection();
            }

            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, "direct");
            channel.basicPublish(exchange, routingKey, null, msg.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Direct模式接收
     */
    public void receiveMsgForDirect(String exchange, String[] routingKey) {

        try {
            if (null == connection) {
                getConnection();
            }
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchange, "direct");
            String queueName = exchange + "_direct_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            for (String key : routingKey) {
                channel.queueBind(queueName, exchange, key);
            }
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
