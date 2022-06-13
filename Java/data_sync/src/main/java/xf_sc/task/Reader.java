package xf_sc.task;

import xf_sc.ObjectSender;

/**
 * @author WBS
 * Date:2022/6/11
 */

public abstract class Reader {
    public static abstract class Task {
        public abstract void startRead(ObjectSender objectSender);
    }
}