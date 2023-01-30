package combiner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * combiner的demo
 * 主要是在reduce阶段减少数据传输
 * 其实WorkCountCombiner和WordCountReducer的代码一样,如果不影响代码逻辑，可以直接 job.setCombinerClass(WordCountReducer.class);
 * 重点：在求平均值的时候，不能用combiner，因为会影响逻辑！！！
 */

public class WorkCountMain {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        //1、获取job
        Configuration cnf = new Configuration();
        cnf.set("fs.defaultFS", "hdfs://192.168.245.101:9000");
        Job job = Job.getInstance(cnf);

        //设置jar包
        job.setJarByClass(WorkCountMain.class);

        //关联map、reduce
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        //设置mapper输出的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //设置最终数据输出的key、value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //Combiner
        job.setCombinerClass(WorkCountCombiner.class);

        //设置数据的输入和输出路径
        FileInputFormat.setInputPaths(job, new Path("/input"));
        FileOutputFormat.setOutputPath(job, new Path("/output"));

        //提交job
        boolean b = job.waitForCompletion(true);

    }

}
