package com.zny.quality.msg;

/**
 * @author WBS
 * Date:2022/5/30
 */

public class MsgSubject extends SubjectAbstract {
    public void sendMsg(String msg) {
        super.notifyObservers(msg);
    }
}
