package outputFormat;


import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author WBS
 * Date 2022-12-03 16:18
 * workCount.WordCountReducer
 */

public class outputFormatReducer extends Reducer<Text, NullWritable, Text, NullWritable> {
    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        for (NullWritable v : values) {
            context.write(key, NullWritable.get());
        }
    }

}
