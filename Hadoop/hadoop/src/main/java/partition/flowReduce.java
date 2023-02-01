package partition;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class flowReduce extends Reducer<Text, FlowBean, Text, FlowBean> {

    private FlowBean outV = new FlowBean();

    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        String str = "";

        //遍历集合累加值
        for (FlowBean flowBean : values) {
            str += flowBean.getStr();
        }

        //封装值
        outV.setStr(str);

        //写出
        context.write(key, outV);
    }
}
