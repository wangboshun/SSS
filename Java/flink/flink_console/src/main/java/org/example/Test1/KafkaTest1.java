package org.example.Test1;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class KafkaTest1 {
    public static void main(String[] args) throws Exception {
// TODO 1.创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "47.118.58.79:9092");
        properties.setProperty("", "");
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<String>("test", new SimpleStringSchema(), properties);
        DataStreamSource<String> streamSource = env.addSource(kafkaConsumer);
        streamSource.print();
        env.execute();
    }
}
