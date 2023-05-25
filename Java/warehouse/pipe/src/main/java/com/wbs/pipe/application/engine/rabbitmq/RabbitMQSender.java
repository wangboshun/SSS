package com.wbs.pipe.application.engine.rabbitmq;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wbs.common.database.base.DataTable;
import com.wbs.pipe.application.engine.base.mq.IMQSender;
import com.wbs.pipe.model.connect.ConnectInfoModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption RabbitMQSender
 */
public class RabbitMQSender implements IMQSender {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ConnectInfoModel connectInfo;
    private SinkInfoModel sinkInfo;

    @Override
    public void config(ConnectInfoModel connectInfo, SinkInfoModel sinkInfo) {
        this.connectInfo = connectInfo;
        this.sinkInfo = sinkInfo;
    }

    @Override
    public void sendData(DataTable dt) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(connectInfo.getHost());
        factory.setPort(connectInfo.getPort());
        factory.setUsername(connectInfo.getUsername());
        factory.setPassword(connectInfo.getPassword());
        factory.setVirtualHost(sinkInfo.getVirtual_host());
        String queueName = sinkInfo.getQueue_name();
        String exchangeName = sinkInfo.getExchange_name();
        BuiltinExchangeType exchangeType = BuiltinExchangeType.DIRECT;
        String routingKey = sinkInfo.getRouting_key();
        if (exchangeName == null) {
            exchangeName = "";
        }
        if (routingKey == null) {
            routingKey = "";
        }
        if (sinkInfo.getExchange_type() != null) {
            exchangeType = EnumUtil.getEnumMap(BuiltinExchangeType.class).get(sinkInfo.getExchange_type().toUpperCase());
        }
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            byte[] body = JSONUtil.toJsonStr(dt).getBytes();

            /*
             * 创建一个队列
             * queue 队列名称
             * durable 是否申明持久队列，true：rabbitmq重启后继续存在
             * exclusive 是否独占，ture：只有该连接能访问
             * autoDelete 是否自动删除，true：服务器不再使用时，将自动删除该队列
             * arguments 队列其它参数
             */
            channel.queueDeclare(queueName, true, false, false, null);

            if (CharSequenceUtil.isNotBlank(exchangeName)) {
                /*
                 * 创建路由
                 * exchange 路由名称
                 * type 路由类型，这里可以用string类型，我直接用了架包里自带的枚举类
                 * durable true：持久化操作，rabbitmq重启后，依然存在
                 * autoDelete true：自动删除，当没被使用时，自动删除该路由
                 * internal
                 * arguments 创建路由时的构造参数
                 */
                channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);

                /*
                 * 队列绑定路由
                 * queue 队列名称
                 * exchange 路由名称
                 * routingKey 路由密钥
                 */
                channel.queueBind(queueName, exchangeName, routingKey);
            }

            /*
             * 推送消息
             * exchange 路由
             * routingKey 路由密钥
             * props 消息的头信息
             * body 消息主体
             */
            channel.basicPublish(exchangeName, routingKey, null, body);
        } catch (Exception e) {
            logger.error("------RabbitMqSender sendData error------", e);
        }
    }
}
