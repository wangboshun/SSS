package partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 自定义分区demo
 * 1、新建类实现Partitioner抽象接口
 * 2、job设置setPartitionerClass
 * 3、job设置setNumReduceTasks
 * 注意：setNumReduceTasks如果设置为1，则最终只会一个分区；如果小于则报IO异常；如果等于则分区正常；如果大于则会产生空文件
 */
public class FlowMain {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration cnf = new Configuration();
        cnf.set("fs.defaultFS", "hdfs://localhost:9000");
        Job job = Job.getInstance(cnf);

        job.setJarByClass(Hadoop3Application.class);

        job.setMapperClass(flowMapper.class);
        job.setReducerClass(flowReduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //自定义分区开始
        job.setPartitionerClass(DefaultPartition.class);
        job.setNumReduceTasks(5);
        //自定义分区结束

        FileInputFormat.setInputPaths(job, new Path("/input3"));
        FileOutputFormat.setOutputPath(job, new Path("/output3"));

        boolean b = job.waitForCompletion(true);

    }
}
