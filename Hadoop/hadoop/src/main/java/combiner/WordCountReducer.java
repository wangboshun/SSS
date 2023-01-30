package combiner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.w3c.dom.Text;

import java.io.IOException;

/**
 * @author WBS
 * Date 2022-12-03 16:18
 * workCount.WordCountReducer
 */

public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
    private LongWritable outV = new LongWritable();

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

        int sum = 0;
        for (LongWritable value : values) {
            sum += value.get();
        }
        outV.set(sum);
        context.write(key, outV);
    }

}
