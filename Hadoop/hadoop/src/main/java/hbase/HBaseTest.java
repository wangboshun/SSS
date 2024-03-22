package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class HBaseTest {

    static Configuration conf = null;
    private static final String ZKconnect = "192.168.123.212:2181";

    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", ZKconnect);
    }

    public static void main(String[] args) {
        String tableName = "student11";

    }
}
