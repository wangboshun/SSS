package org.example.Test1;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.example.model.Event;

public class FlatMapTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
        DataStreamSource<Event> source = env.fromElements(
                new Event("aaa", "qq"),
                new Event("bbb", "baidu")
        );
        //方式一
        SingleOutputStreamOperator<String> result = source.flatMap(new MyFlatMap());

        SingleOutputStreamOperator<String> result2 = source.flatMap((Event value, Collector<String> out) -> {
            if (value.user.equals("aaa")) {
                out.collect(value.user);
            } else {
                out.collect(value.user);
                out.collect(value.url);
            }
        }).returns(new TypeHint<String>() {
        });

//        result.print();
        result2.print();
        env.execute();
    }

    public static class MyFlatMap implements FlatMapFunction<Event,String>{


        @Override
        public void flatMap(Event value, Collector<String> out) throws Exception {
            out.collect(value.user);
            out.collect(value.url);
        }
    }
}
