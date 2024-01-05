package org.example.Test1;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.example.model.Event;

import java.util.Properties;

//从kafka读取字符串，转换成实体后再输出
public class KafkaSinkTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "47.118.58.79:9092");
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<String>("test", new SimpleStringSchema(), properties);
        DataStreamSource<String> streamSource = env.addSource(kafkaConsumer);

        SingleOutputStreamOperator<String> result = streamSource.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                String[] fields = value.split(",");
                return new Event(fields[0], fields[1], fields[2]).toString();
            }
        });

        result.addSink(new FlinkKafkaProducer<String>( "test2",new SimpleStringSchema(),properties));

        env.execute();
    }
}
