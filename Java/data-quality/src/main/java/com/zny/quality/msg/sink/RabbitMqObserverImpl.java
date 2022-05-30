package com.zny.quality.msg.sink;

import com.zny.quality.msg.MsgObserverInterface;

/**
 * Rabbitmq中间件
 * @author WBS
 * Date:2022/5/30
 */

public class RabbitMqObserverImpl implements MsgObserverInterface {

    private String msg;

    public RabbitMqObserverImpl(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        System.out.println("开始使用RabbitMq发送消息：" + getMsg());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("RabbitMq发送消息结束");
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
