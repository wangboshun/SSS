package xf_sc.thread;

import xf_sc.RecordSender;
import xf_sc.mysql.MySqlReader;
import xf_sc.task.Reader;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class ReaderRunner implements Runnable {

    private RecordSender recordSender;

    public void setRecordSender(RecordSender recordSender) {
        this.recordSender = recordSender;
    }

    @Override
    public void run() {
        Reader.Task taskReader = new MySqlReader.Task();
        taskReader.startRead(recordSender);
    }
}
