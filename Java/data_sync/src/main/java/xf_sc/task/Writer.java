package xf_sc.task;

import xf_sc.ObjectReceiver;

/**
 * @author WBS
 * Date:2022/6/11
 */
public abstract class Writer {
    public abstract static class Task {
        public abstract void startWrite(ObjectReceiver lineReceiver);

    }
}
