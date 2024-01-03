package org.example.Test1;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.model.Event;

public class FilterTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
        DataStreamSource<Event> source = env.fromElements(
                new Event("aaa", "qq"),
                new Event("bbb", "baidu")
        );
        //方式一
        SingleOutputStreamOperator<Event> result = source.filter(new MyFilter());

        //方式二
        SingleOutputStreamOperator<Event> result2 = source.filter(new FilterFunction<Event>() {

            @Override
            public boolean filter(Event value) throws Exception {
                return value.user.equals("aaa");
            }
        });

        SingleOutputStreamOperator<Event> result3 = source.filter(x -> x.user.equals("aaa"));

        result.print();
        result2.print();
        result3.print();
        env.execute();
    }

    public static class MyFilter implements FilterFunction<Event> {


        @Override
        public boolean filter(Event value) throws Exception {
            return value.user.equals("aaa");
        }
    }
}
