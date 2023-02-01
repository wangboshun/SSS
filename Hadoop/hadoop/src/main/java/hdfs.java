import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

/**
 * @author WBS
 * Date 2022-11-26 11:02
 * Main
 */

public class hdfs {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        try {
            info();
//            put();
//            get();
        } catch (Exception e) {

        }

        System.out.println("hello hadoop!");
    }

    private static void put() {
        try {
            URL url = new URL("hdfs://172.18.0.3:9000");
            Configuration configuration = new Configuration();
            FileSystem fs = FileSystem.get(url.toURI(), configuration, "wbs");
            fs.copyFromLocalFile(true, true, new Path("C:\\Users\\WBS\\Desktop\\chabushuj.csv"), new Path("/test1"));
            fs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void get() throws URISyntaxException, IOException, InterruptedException {
        try {
            URL url = new URL("hdfs://172.18.0.3:9000");
            Configuration configuration = new Configuration();
            FileSystem fs = FileSystem.get(url.toURI(), configuration, "wbs");
            fs.copyToLocalFile(false, new Path("/test1/chabushuj.csv"), new Path("C:\\Users\\WBS\\Desktop\\test\\aaa.csv"));
            fs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void mkdir() throws IOException, URISyntaxException, InterruptedException {
        URL url = new URL("hdfs://172.18.0.3:9000");
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(url.toURI(), configuration, "wbs");
        fs.mkdirs(new Path("/test2"));

        fs.close();
    }

    private static void delete() throws IOException, URISyntaxException, InterruptedException {
        URL url = new URL("hdfs://172.18.0.3:9000");
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(url.toURI(), configuration, "wbs");
        fs.delete(new Path("/test1"), true);

        fs.close();
    }

    private static void move() throws IOException, URISyntaxException, InterruptedException {
        URL url = new URL("hdfs://172.18.0.3:9000");
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(url.toURI(), configuration, "wbs");
        fs.rename(new Path("/test2"), new Path("/test1"));

        fs.close();
    }

    private static void info() throws IOException, URISyntaxException, InterruptedException {
        try {
            System.out.println("---info---");
            URL url = new URL("hdfs://172.18.0.3:9000");
            Configuration configuration = new Configuration();
            FileSystem fs = FileSystem.get(url.toURI(), configuration, "root");
            RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/"), true);
            while (files.hasNext()) {
                LocatedFileStatus status = files.next();
                System.out.println("文件名：" + status.getPath().getName());
                System.out.println("地址：" + status.getPath());
                System.out.println("长度：" + status.getLen());
                System.out.println("权限：" + status.getPermission());
                System.out.println("类型：" + (status.isFile() ? "文件" : "文件夹"));

                //查看块信息
                BlockLocation[] blockLocations = status.getBlockLocations();
                System.out.println(Arrays.toString(blockLocations));
            }
            fs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


}
