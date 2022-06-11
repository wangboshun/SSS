package xf_sc.mysql;

import xf_sc.RecordSender;
import xf_sc.task.Reader;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class MySqlReader extends Reader {


    public static class Task extends Reader.Task {

        @Override
        public void startRead(RecordSender recordSender) {

            for (int i = 0; i < 100; i++) {
                String o = "hello world " + i;
                recordSender.sendToWriter(o);
                System.out.println("send :"+o);
            }
        }
    }


}