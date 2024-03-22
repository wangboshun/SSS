package workCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author WBS
 * Date 2022-12-03 16:01
 * workCount.hadoop_2
 */

public class WorkCountMain {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        // 1、获取job
        Configuration cnf = new Configuration();
//          cnf.set("fs.defaultFS", "hdfs://localhost:9000");
        Job job = Job.getInstance(cnf);

        // 设置jar包
        job.setJarByClass(WorkCountMain.class);

        // 关联map、reduce
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 设置mapper输出的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        // 设置最终数据输出的key、value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 设置数据的输入和输出路径
        FileInputFormat.setInputPaths(job, new Path("D:\\SSS\\Hadoop\\hadoop\\src\\main\\java\\workCount\\input"));
        FileOutputFormat.setOutputPath(job, new Path("D:\\SSS\\Hadoop\\hadoop\\src\\main\\java\\workCount\\output"));

        // 提交job
        boolean b = job.waitForCompletion(true);

    }

}
