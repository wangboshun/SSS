package flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class flowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

    private Text outK = new Text();
    private FlowBean outV = new FlowBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //读取数据
        String line = value.toString();

        //拆分
        String[] split = line.split("\t");

        //抽取数据
        String phone = split[0].trim();
        String upFlow = split[split.length - 3].trim();
        String downFlow = split[split.length - 2].trim();

        //封装数据
        outK.set(phone);
        outV.setUpFlow(Long.parseLong(upFlow));
        outV.setDownFlow(Long.parseLong(downFlow));
        outV.setSumFlow();

        //写出数据
        context.write(outK, outV);
    }

}
