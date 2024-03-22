package org.example.Test1;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.model.Event;

public class RichTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Event> source = env.fromElements(
                new Event("aaa", "qq"),
                new Event("bbb", "baidu"),
                new Event("ccc", "360")
        );
        //方式一
        source.map(new MyRichMapper()).print();
        env.execute();
    }

    public static class MyRichMapper extends RichMapFunction<Event, String> {

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            System.out.println("open生命周期");
        }

        @Override
        public void close() throws Exception {
            super.close();
            System.out.println("close生命周期");
        }

        @Override
        public String map(Event value) throws Exception {
            return value.user;
        }
    }
}
