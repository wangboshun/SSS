package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelTest {
    public static void main(String[] args) throws IOException {
        transTo();
    }

    /**
     * 文件读
     */
    public static void read() throws IOException {
        String path = "C:\\Users\\WBS\\Desktop\\状态码.txt";
        FileInputStream fis = new FileInputStream(path);
        FileChannel fileChannel = fis.getChannel();
        ByteBuffer buffer_1 = ByteBuffer.allocate(1024);

        int bytesRead = fileChannel.read(buffer_1);
        while (bytesRead != -1) {
            buffer_1.flip();
            byte[] bytes = new byte[bytesRead];
            buffer_1.get(bytes);
            System.out.println("读取到的数据：" + new String(bytes, 0, bytes.length));
            buffer_1.clear();
            bytesRead = fileChannel.read(buffer_1);
        }
    }

    /**
     * 文件写
     */
    public static void write() throws IOException {
        String path = "C:\\Users\\WBS\\Desktop\\状态码.txt";
        FileOutputStream fos = new FileOutputStream(path);
        FileChannel fileChannel = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < 10; i++) {
            buffer.clear();
            buffer.put(("write data [" + i + "] to file \r\n").getBytes());
            buffer.flip();
            fileChannel.write(buffer);
        }
        fileChannel.close();
    }

    /**
     * 文件拷贝
     */
    public static void copy() throws IOException {
        String path_1 = "C:\\Users\\WBS\\Desktop\\状态码.txt";
        String path_2 = "C:\\Users\\WBS\\Desktop\\状态码2.txt";

        FileInputStream fis = new FileInputStream(path_1);
        FileOutputStream fos = new FileOutputStream(path_2);

        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (fisChannel.read(buffer) > 0) {
            buffer.flip();
            fosChannel.write(buffer);
            buffer.clear();
        }
        fisChannel.close();
        fosChannel.close();
        System.out.println("复制完成！");
    }

    /**
     * 目标通道中去复制原通道数据
     */
    public static void transform() throws IOException {
        String path_1 = "C:\\Users\\WBS\\Desktop\\状态码.txt";
        String path_2 = "C:\\Users\\WBS\\Desktop\\状态码2.txt";

        FileInputStream fis = new FileInputStream(path_1);
        FileOutputStream fos = new FileOutputStream(path_2);

        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        fosChannel.transferFrom(fisChannel, fisChannel.position(), fisChannel.size());

        fisChannel.close();
        fosChannel.close();
    }

    /**
     * 原通道数据复制到目标通道
     */
    public static void transTo() throws IOException {
        String path_1 = "C:\\Users\\WBS\\Desktop\\状态码.txt";
        String path_2 = "C:\\Users\\WBS\\Desktop\\状态码2.txt";

        FileInputStream fis = new FileInputStream(path_1);
        FileOutputStream fos = new FileOutputStream(path_2);

        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        fisChannel.transferTo(fisChannel.position(), fisChannel.size(), fosChannel);

        fisChannel.close();
        fosChannel.close();
    }
}
