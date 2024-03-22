package sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 排序demo，对已有的flow的demo数据进行流量排序
 * 按照流量进行排序，那么bean是key，手机号是value
 * mapper、reduce的key和value类型要对应的改变
 */
public class FlowMain {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration configuration = new Configuration();

        Job job = Job.getInstance(configuration);

        job.setJarByClass(FlowMain.class);

        job.setMapperClass(flowMapper.class);
        job.setReducerClass(flowReduce.class);

        // 按照流量进行排序，那么bean是key，手机号是value
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        FileInputFormat.setInputPaths(job, new Path("/output"));  // 这里是上次flow的结果
        FileOutputFormat.setOutputPath(job, new Path("/output2"));

        boolean b = job.waitForCompletion(true);

    }
}
