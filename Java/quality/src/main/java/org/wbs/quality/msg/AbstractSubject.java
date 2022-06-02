package org.wbs.quality.msg;


import org.wbs.quality.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author WBS
 * Date:2022/5/30
 */

public abstract class AbstractSubject {
    private final List<MsgObserver> list = new ArrayList<>();

    /**
     * 添加观察者
     */
    public void addObserver(MsgObserver observer) {
        list.add(observer);
    }

    /**
     * 删除观察者
     */
    public void removeObserver(MsgObserver observer) {
        list.remove(observer);
    }

    /**
     * 通知被观察者
     */
    public void notifyObservers() {
        System.out.println("通知所有发信息中间件");
        ThreadPoolExecutor executor = ThreadUtils.createThreadPool("AbstractSubject");
        for (MsgObserver observer : list) {
            executor.execute(observer);
        }
    }
}
