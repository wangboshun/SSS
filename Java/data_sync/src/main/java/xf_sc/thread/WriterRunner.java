package xf_sc.thread;

import xf_sc.ObjectReceiver;
import xf_sc.mysql.MySqlWrite;
import xf_sc.task.Writer;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class WriterRunner implements Runnable {

    private ObjectReceiver objectReceiver;

    public void setRecordReceiver(ObjectReceiver o) {
        this.objectReceiver = o;
    }

    @Override
    public void run() {
        Writer.Task taskWriter = new MySqlWrite.Task();
        taskWriter.startWrite(objectReceiver);
    }
}
