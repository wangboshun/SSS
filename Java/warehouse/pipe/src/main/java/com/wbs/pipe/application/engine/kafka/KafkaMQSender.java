package com.wbs.pipe.application.engine.kafka;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.wbs.common.database.base.DataTable;
import com.wbs.pipe.application.engine.base.mq.IMQSender;
import com.wbs.pipe.model.connect.ConnectInfoModel;
import com.wbs.pipe.model.sink.SinkInfoModel;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Set;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption KafkaMQSender
 */
public class KafkaMQSender implements IMQSender {
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
        Properties properties = new Properties();
        // 指定集群节点
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, connectInfo.getHost() + ":" + connectInfo.getPort());
        // 发送消息，网络传输，需要对key和value指定对应的序列化类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 创建 AdminClient 对象
        try (AdminClient client = AdminClient.create(properties)) {
            // 获取 topic 列表
            Set<String> topics = client.listTopics().names().get();
            if (!topics.contains(sinkInfo.getTopic())) {
                client.createTopics(Lists.newArrayList(new NewTopic(sinkInfo.getTopic(), 1, (short) 1)));
            }
            // 创建消息生产者对象
            KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
            String body = JSONUtil.toJsonStr(dt);
            ProducerRecord<String, String> record = new ProducerRecord<>(sinkInfo.getTopic(), body);
            // 发送
            producer.send(record);
            producer.close();
        } catch (Exception e) {
            logger.error("------KafkaMQSender sendData error------", e);
        }
    }
}
