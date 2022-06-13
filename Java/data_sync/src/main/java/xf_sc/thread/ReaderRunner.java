package xf_sc.thread;

import xf_sc.ObjectSender;
import xf_sc.mysql.MySqlReader;
import xf_sc.task.Reader;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class ReaderRunner implements Runnable {

    private ObjectSender objectSender;

    public void setRecordSender(ObjectSender o) {
        this.objectSender = o;
    }

    @Override
    public void run() {
        Reader.Task taskReader = new MySqlReader.Task();
        taskReader.startRead(objectSender);
    }
}
