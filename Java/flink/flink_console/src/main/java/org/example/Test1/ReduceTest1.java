package org.example.Test1;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.model.Event;

public class ReduceTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
         env.setParallelism(1);
        DataStreamSource<Event> source = env.fromElements(
                new Event("a", "1", "a1", 1),
                new Event("b", "2", "b1", 2),
                new Event("a", "3", "a2", 3),
                new Event("b", "4", "b2", 4),
                new Event("a", "5", "a3", 5),
                new Event("b", "6", "b3", 6),
                new Event("b", "6", "b3", 1)
        );

        //统计每个用户访问频次
        SingleOutputStreamOperator<Tuple2<String, Long>> reduce = source.map(new MapFunction<Event, Tuple2<String, Long>>() {

            @Override
            public Tuple2<String, Long> map(Event value) throws Exception {
                return Tuple2.of(value.user, 1L);
            }
        }).keyBy(x -> x.f0).reduce(new ReduceFunction<Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2) throws Exception {
                return Tuple2.of(value1.f0, value1.f1 + value2.f1);
            }
        });

        //获取当前最活跃的用户
        SingleOutputStreamOperator<Tuple2<String, Long>> result = reduce.keyBy(x -> "key").reduce(new ReduceFunction<Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2) throws Exception {
                return value1.f1 > value2.f1 ? value1 : value2;
            }
        });

        reduce.print();
        env.execute();
    }
}
