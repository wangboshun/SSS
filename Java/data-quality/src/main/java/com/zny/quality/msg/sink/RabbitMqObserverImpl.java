package com.zny.quality.msg.sink;

import com.zny.quality.msg.MsgObserver;

/**
 * @author WBS
 * Date:2022/5/30
 */

public class RabbitMqObserverImpl implements MsgObserver {
    @Override
    public void sendMsg(String msg) {
        System.out.println("使用RabbitMq发送消息："+msg);
    }
}
