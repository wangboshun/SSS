package partition;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class flowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

    private Text outK = new Text();
    private FlowBean outV = new FlowBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 读取数据
        String line = value.toString();

        // 拆分
        String[] split = line.split(",");

        // 抽取数据
        String phone = split[1].trim();
        String str = split[2].trim();

        // 封装数据
        outK.set(phone);
        outV.setStr(str);

        // 写出数据
        context.write(outK, outV);
    }

}
