package org.example.Test1;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.model.Event;

//简单聚合，MAX函数
public class AggTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
        DataStreamSource<Event> source = env.fromElements(
                new Event("a", "1","a1"),
                new Event("b", "2","b1"),
                new Event("a", "3","a2"),
                new Event("b", "4","b2"),
                new Event("a", "5","a3"),
                new Event("b", "6","b3")
        );

        //保留最前面的user、type值不变，但是url会变
        source.keyBy(new KeySelector<Event, String>() {

            @Override
            public String getKey(Event value) throws Exception {
                return value.user;
            }
        }).max("url").print("max");


        //user、type、url都会变
        source.keyBy(x->x.user).maxBy("url").print("maxBy");

        env.execute();
    }

}
