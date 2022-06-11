package xxf_sc;

import org.junit.jupiter.api.Test;
import xf_sc.Channel;
import xf_sc.Exchanger;
import xf_sc.thread.ReaderRunner;
import xf_sc.thread.WriterRunner;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class task {

    @Test
    public void test1() throws InterruptedException {

        Channel channel = new Channel();

        ReaderRunner readerRunner = new ReaderRunner();
        readerRunner.setRecordSender(new Exchanger(channel));

        WriterRunner writerRunner = new WriterRunner();
        writerRunner.setRecordReceiver(new Exchanger(channel));

        Thread writerThread = new Thread(writerRunner, "writerThread");
        Thread senderThread = new Thread(readerRunner, "senderThread");


        writerThread.start();
        senderThread.start();


        Thread.sleep(2000);
    }

}
