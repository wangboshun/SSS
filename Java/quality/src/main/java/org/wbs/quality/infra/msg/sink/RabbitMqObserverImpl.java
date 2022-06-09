package org.wbs.quality.infra.msg.sink;


import org.wbs.quality.ApplicationMain;
import org.wbs.quality.infra.msg.MsgObserver;
import org.wbs.quality.infra.utils.ContextUtils;
import org.wbs.quality.infra.utils.RabbitMqUtils;

/**
 * Rabbitmq中间件
 *
 * @author WBS
 * Date:2022/5/30
 */

public class RabbitMqObserverImpl implements MsgObserver {

    private final String msg;

    public RabbitMqObserverImpl(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        System.out.println("开始使用RabbitMq发送消息：" + msg);
        RabbitMqUtils r = ContextUtils.getBean(RabbitMqUtils.class);
        r.sendMsgForWork("RabbitMqObserverImpl", msg);
        System.out.println("RabbitMq发送消息结束");
    }
}
