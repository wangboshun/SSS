package sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class flowMapper extends Mapper<LongWritable, Text, FlowBean, Text> {

    private Text outV = new Text();
    private FlowBean outK = new FlowBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //读取数据
        String line = value.toString();

        //拆分
        String[] split = line.split("\t");

        //抽取数据
        String phone = split[0].trim();
        String upFlow = split[1].trim();
        String downFlow = split[2].trim();

        //封装数据
        outV.set(phone);
        outK.setUpFlow(Long.parseLong(upFlow));
        outK.setDownFlow(Long.parseLong(downFlow));
        outK.setSumFlow();

        //写出数据
        context.write(outK, outV);
    }

}
