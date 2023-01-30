package partition;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 把不同的手机号分散在不同的文件中
 * 如：135、136、137开头的在一个文件，其他的在一个文件
 * 假设有5个分区
 */
public class DefaultPartition extends Partitioner<Text, FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int i) {

        String phone = text.toString();//手机号

        phone = phone.substring(0, 3);

        int partition;

        if ("135".equals(phone)) {
            partition = 0;
        }
        if ("136".equals(phone)) {
            partition = 1;
        }
        if ("137".equals(phone)) {
            partition = 2;
        }
        if ("138".equals(phone)) {
            partition = 3;
        } else {
            partition = 4;
        }

        return partition;
    }
}
