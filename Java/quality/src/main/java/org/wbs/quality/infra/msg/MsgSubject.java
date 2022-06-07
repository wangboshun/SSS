package org.wbs.quality.infra.msg;

/**
 * @author WBS
 * Date:2022/5/30
 */

public class MsgSubject extends AbstractSubject {

    /**
     * 执行
     */
    public void sendMsg() {
        super.notifyObservers();
    }
}
