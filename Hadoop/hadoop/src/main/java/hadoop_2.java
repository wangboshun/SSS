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
 * hadoop_2
 */

public class hadoop_2 {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Configuration cnf = new Configuration();
        cnf.set("fs.defaultFS","hdfs://192.168.245.101:9000");
        Job job = Job.getInstance(cnf);

        job.setJarByClass(hadoop_2.class);

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(job, new Path("/input"));
        FileOutputFormat.setOutputPath(job, new Path("/output"));

        boolean b = job.waitForCompletion(true);

    }

}
