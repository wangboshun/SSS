package org.example.Test1;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.model.Event;

public class MapTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
        DataStreamSource<Event> source = env.fromElements(
                new Event("aaa", "qq"),
                new Event("bbb", "baidu")
        );
        //方式一
        SingleOutputStreamOperator<String> result = source.map(new MyMapper());

        //方式二
        SingleOutputStreamOperator<String> result2 = source.map(new MapFunction<Event, String>() {
            @Override
            public String map(Event value) throws Exception {
                return value.user;
            }
        });

        SingleOutputStreamOperator<String> result3 = source.map(x -> x.user);

        result.print();
        result2.print();
        result3.print();
        env.execute();
    }

    public static class MyMapper implements MapFunction<Event, String> {

        @Override
        public String map(Event value) throws Exception {
            return value.user;
        }
    }
}
