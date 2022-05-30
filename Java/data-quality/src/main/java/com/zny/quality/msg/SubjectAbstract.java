package com.zny.quality.msg;

import com.zny.quality.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author WBS
 * Date:2022/5/30
 */

public abstract class SubjectAbstract {
    private final List<MsgObserverInterface> list = new ArrayList<>();

    /**
     * 添加观察者
     */
    public void addObserver(MsgObserverInterface observer) {
        list.add(observer);
    }

    /**
     * 删除观察者
     */
    public void removeObserver(MsgObserverInterface observer) {
        list.remove(observer);
    }

    /**
     * 通知被观察者
     */
    public void notifyObservers() {
        System.out.println("通知所有发信息中间件");
        ThreadPoolExecutor executor = ThreadUtils.createThreadPool();
        for (MsgObserverInterface observer : list) {
            executor.execute(observer);
        }
    }
}
