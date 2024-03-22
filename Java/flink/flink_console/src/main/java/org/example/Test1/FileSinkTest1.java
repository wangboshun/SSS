package org.example.Test1;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.example.model.Event;

import java.util.concurrent.TimeUnit;


//写入到文件，四个线程
public class FileSinkTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
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

        StreamingFileSink<String> sink = StreamingFileSink.<String>forRowFormat(new Path("./output/"),
                new SimpleStringEncoder<>("UTF-8"))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withMaxPartSize(1024*1024*1024)  //文件大小，单位字节，这里设置是1g
                                .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))  //时间间隔，单位毫秒，这里设置是15分钟
                                .withInactivityInterval(TimeUnit.MINUTES.toMillis(5)).build()  //长时间无数据，单位毫秒，这里设置是5分钟
                )
                .build();

        source.map(x->x.toString()).addSink(sink);
        env.execute();
    }
}
