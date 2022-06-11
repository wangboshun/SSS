package xf_sc.mysql;

import xf_sc.RecordReceiver;
import xf_sc.task.Writer;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class MySqlWrite extends Writer {

    public static class Task extends Writer.Task {

        @Override
        public void startWrite(RecordReceiver recordReceiver) {
            Object o;
            while ((o = recordReceiver.getFromReader()) != null) {
                System.out.println("receive :" + o);
            }
        }
    }
}
