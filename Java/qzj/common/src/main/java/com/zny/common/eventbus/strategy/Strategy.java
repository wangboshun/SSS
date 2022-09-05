package com.zny.common.eventbus.strategy;

import com.zny.common.eventbus.core.EventConsumer;
import com.zny.common.eventbus.core.GroupConsumer;


/**
 * 消费者选取策略
 */
public interface Strategy {

    /**
     * 选择合适的事件订阅者
     *
     * @param groupConsumer 事件订阅者集合
     * @return 事件订阅者
     */
    EventConsumer select(GroupConsumer groupConsumer);
}
