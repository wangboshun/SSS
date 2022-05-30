package com.zny.quality.msg;

/**
 * @author WBS
 * Date:2022/5/30
 */

public class MsgSubject extends SubjectAbstract {

    /**
     * 执行
     */
    public void sendMsg() {
        super.notifyObservers();
    }
}
