package org.example.Test1;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.example.model.Event;

public class PartitionTest1 {

    public static void test1() throws Exception {
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
        //随机分区
        //source.shuffle().print().setParallelism(4);

        //轮询分区
        //source.rebalance().print().setParallelism(4);


        env.execute();
    }

    public static void test2() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2 4 6 8 0 在1 2分区
        //1 3  5 7 在3 4分区
        //重缩放分区
        env.addSource(new RichParallelSourceFunction<Integer>() {

            @Override
            public void run(SourceContext<Integer> ctx) throws Exception {
                for (int i = 0; i < 10; i++) {
                    //将奇偶数分别发到0和1号分区
                    if (i % 2 == getRuntimeContext().getIndexOfThisSubtask()) {
                        ctx.collect(i);
                    }
                }
            }

            @Override
            public void cancel() {

            }
        }).setParallelism(2).rescale().print().setParallelism(4);

        env.execute();
    }

    public static void test3() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.fromElements(1, 2, 3, 4, 5, 6, 7, 8, 9).partitionCustom(new Partitioner<Integer>() {
            //这里只设置了两个分区，奇数第二个分区，偶数第一个分区
            @Override
            public int partition(Integer key, int numPartitions) {
                return key % 2;
            }
        }, new KeySelector<Integer, Integer>() {
            @Override
            public Integer getKey(Integer value) throws Exception {
                return value;
            }
        }).print().setParallelism(4);

        env.execute();
    }

    public static void main(String[] args) throws Exception {
        test3();
    }
}
