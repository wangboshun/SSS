package xf_sc.thread;

import xf_sc.RecordReceiver;
import xf_sc.mysql.MySqlWrite;
import xf_sc.task.Writer;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class WriterRunner implements Runnable {

    private RecordReceiver recordReceiver;

    public void setRecordReceiver(RecordReceiver receiver) {
        this.recordReceiver = receiver;
    }

    @Override
    public void run() {
        Writer.Task taskWriter = new MySqlWrite.Task();
        taskWriter.startWrite(recordReceiver);
    }
}
