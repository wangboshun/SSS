package org.wbs.quality.infra.msg.sink;


import org.wbs.quality.infra.msg.MsgObserver;

/**
 * Redis中间件
 *
 * @author WBS
 * Date:2022/5/30
 */

public class RedisObserverImpl implements MsgObserver {
    private String msg;

    public RedisObserverImpl(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        System.out.println("开始使用Redis发送消息：" + getMsg());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Redis发送消息结束");
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
