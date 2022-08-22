package xf_sc.mysql;

import xf_sc.ObjectSender;
import xf_sc.task.Reader;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class MySqlReader extends Reader {


    public static class Task extends Reader.Task {

        @Override
        public void startRead(ObjectSender objectSender) {

            for (int i = 0; i < 1_000_000000; i++) {
                String o = "hello world " + i;
                objectSender.sendToWriter(o);
                System.out.println("send :"+o);
            }
        }
    }


}