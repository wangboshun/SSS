package com.zny.quality.msg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WBS
 * Date:2022/5/30
 */

public abstract class SubjectAbstract {
    private final List<MsgObserver> list = new ArrayList<>();

    public void addObserver(MsgObserver observer) {
        list.add(observer);
    }

    public void removeObserver(MsgObserver observer) {
        list.remove(observer);
    }

    public void notifyObservers(String msg) {
        System.out.println("通知所有发信息中间件");
        for (MsgObserver observer : list) {
            observer.sendMsg(msg);
        }
    }
}
