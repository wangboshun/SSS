package org.example.Test1;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class StreamWordCountTest1 {
    public static void main(String[] args) throws Exception {
        // 1.创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); //设置并行度
        // 2.从文件中读取数据
        DataStreamSource<String> inputDS = env.readTextFile("/home/wbs/flink/words.txt");
        // 3.将数据转为2元组：(word, 1)
        SingleOutputStreamOperator<Tuple2<String, Long>> wordAndOneDS = inputDS
                .flatMap((String line, Collector<String> words) -> {
                    Arrays.stream(line.split(" ")).forEach(words::collect);
                })
                .returns(Types.STRING) //声明返回类型
                .map(word -> Tuple2.of(word, 1L))
                .returns(Types.TUPLE(Types.STRING, Types.LONG)); //声明返回类型
        // 4.按照2元组的word进行group
        KeyedStream<Tuple2<String, Long>, String> wordCountKS = wordAndOneDS.keyBy(t -> t.f0);
        // 5.分组内进行聚合统计
        SingleOutputStreamOperator<Tuple2<String, Long>> wordCount = wordCountKS.sum(1);
        // 6.打印输出
        wordCount.print();
        // 7.执行
        env.execute();
    }
}
