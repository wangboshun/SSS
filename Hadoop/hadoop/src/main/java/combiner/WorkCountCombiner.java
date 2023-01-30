package combiner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.w3c.dom.Text;

import java.io.IOException;

/**
 * combiner
 * 其实WorkCountCombiner和WordCountReducer的代码一样
 */
public class WorkCountCombiner extends Reducer<Text, LongWritable, Text, LongWritable> {
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
