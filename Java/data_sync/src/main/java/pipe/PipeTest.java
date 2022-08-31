package pipe;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author WBS
 * Date:2022/8/30
 */

public class PipeTest {

    public static void main(String[] args) throws IOException, InterruptedException {


        for (int i = 0; i < 10; i++) {

            // 创建一个发送者对象
            Sender sender = new Sender();
            // 创建一个接收者对象
            Receiver receiver = new Receiver();
            // 获取输出管道流
            PipedOutputStream outputStream = sender.getOutputStream();
            // 获取输入管道流
            PipedInputStream inputStream = receiver.getInputStream();
            // 链接两个管道，这一步很重要，把输入流和输出流联通起来
            outputStream.connect(inputStream);
            // 启动发送者线程
            sender.start();
            // 启动接收者线程
            receiver.start();


            Thread.sleep(2000);
        }

    }

}

class Sender extends Thread {

    // 声明一个 管道输出流对象 作为发送方
    private PipedOutputStream outputStream = new PipedOutputStream();

    public PipedOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void run() {
        String msg = "Hello World";
        try {
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输出流
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 接收线程
 *
 * @author yuxuan
 */
class Receiver extends Thread {

    // 声明一个 管道输入对象 作为接收方
    private PipedInputStream inputStream = new PipedInputStream();

    public PipedInputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024];
        try {
            // 通过read方法 读取长度
            int len = inputStream.read(buf);
            System.out.println(new String(buf, 0, len));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输入流
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}