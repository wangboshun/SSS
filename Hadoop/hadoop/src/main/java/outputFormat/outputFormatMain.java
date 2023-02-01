package outputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 自定义输出源
 */

public class outputFormatMain {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

           //1、获取job
           Configuration cnf = new Configuration();
           cnf.set("fs.defaultFS", "hdfs://localhost:9000");
           Job job = Job.getInstance(cnf);
   
           //设置jar包
           job.setJarByClass(outputFormatMain.class);
   
           //关联map、reduce
           job.setMapperClass(outputFormatMapper.class);
           job.setReducerClass(outputFormatReducer.class);
   
           //设置mapper输出的key、value类型
           job.setMapOutputKeyClass(Text.class);
           job.setMapOutputValueClass(NullWritable.class);
   
           //设置最终数据输出的key、value类型
           job.setOutputKeyClass(Text.class);
           job.setOutputValueClass(NullWritable.class);
   
           //设置自定义输出规则
           job.setOutputFormatClass(defaultOutputFormat.class);
   
           //设置输入、输出源
           FileInputFormat.setInputPaths(job, new Path("/input2"));
           FileOutputFormat.setOutputPath(job, new Path("/output2"));
   
           //提交job
           boolean b = job.waitForCompletion(true);

    }

}
