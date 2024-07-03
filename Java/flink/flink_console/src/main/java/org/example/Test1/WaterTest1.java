package org.example.Test1;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.model.Event;

import java.time.Duration;

public class WaterTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.getConfig().setAutoWatermarkInterval(100);//100毫秒

        DataStreamSource<Event> stream = env.fromElements(
                new Event("a", "1", "a1", 1),
                new Event("b", "2", "b1", 2),
                new Event("a", "3", "a2", 3),
                new Event("b", "4", "b2", 4),
                new Event("a", "5", "a3", 5),
                new Event("b", "6", "b3", 6),
                new Event("b", "6", "b3", 1)
        );
//                .assignTimestampsAndWatermarks(
//                WatermarkStrategy.<Event>forMonotonousTimestamps() //有序流watermark生成
//                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
//                            @Override
//                            public long extractTimestamp(Event element, long recordTimestamp) {
//                                return element.cnt;   //指定时间戳
//                            }
//                        })

//                WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(2))  //乱序流watermark生成
//                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
//                            @Override
//                            public long extractTimestamp(Event element, long recordTimestamp) {
//                                return element.cnt;   //指定时间戳
//                            }
//                        })
//        );


        env.execute();
    }
}
