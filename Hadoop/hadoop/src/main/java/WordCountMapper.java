import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author WBS
 * Date 2022-12-03 16:04
 * WordCountMapper
 */

public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        //读取一行  aa bb
        String line = value.toString();

        //根据符合分割
        //aa
        //bb
        String[] words = line.split(" ");

        Text outK = new Text();
        LongWritable outV = new LongWritable(1);

        //循环输出
        for (String word : words) {
            outK.set(word);
            context.write(outK, outV);
        }
    }
}
