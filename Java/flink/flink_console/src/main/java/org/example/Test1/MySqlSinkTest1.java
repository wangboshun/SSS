package org.example.Test1;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.example.model.Event;

import java.util.Properties;

public class MySqlSinkTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Event> source = env.fromElements(
                new Event("aaa", "1"),
                new Event("bbb", "2"),
                new Event("ccc", "3"),
                new Event("aaa", "4"),
                new Event("bbb", "5"),
                new Event("ccc", "6"),
                new Event("aaa", "7"),
                new Event("bbb", "8"),
                new Event("ccc", "9")
        );

        source.addSink(JdbcSink.sink("INSERT INTO test1 (user,url) values(?,?)",((statement,event)->{
            statement.setString(1,event.user);
            statement.setString(2,event.url);
        }),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:mysql://127.0.0.1:3306/flink?useUnicode=true&characterEncoding=utf8&useSSL=false")
                        .withDriverName("com.mysql.cj.jdbc.Driver")
                        .withUsername("root")
                        .withPassword("123456").build()
        ));

        env.execute();
    }
}
