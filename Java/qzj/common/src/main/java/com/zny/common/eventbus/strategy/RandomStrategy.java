package com.zny.common.eventbus.strategy;

import com.google.common.collect.Maps;
import com.zny.common.eventbus.core.EventConsumer;
import com.zny.common.eventbus.core.GroupConsumer;

import java.util.Map;
import java.util.Random;

/**
 * Random策略
 */
public class RandomStrategy implements Strategy {

    private Map<String, Random> groupRandom;

    /**
     * 选择合适的事件订阅者
     *
     * @param groupConsumer 事件订阅者集合
     * @return 事件订阅者
     */
    public EventConsumer select(GroupConsumer groupConsumer) {
        Random random = getRandom(groupConsumer);
        int number = random.nextInt(groupConsumer.getEventConsumers().size());
        return groupConsumer.getEventConsumers().get(number);
    }

    /**
     * 获取分组对应的消费者
     *
     * @param groupConsumer 分组对应的消费者
     * @return
     */
    public Random getRandom(GroupConsumer groupConsumer) {
        if (groupRandom == null) {
            groupRandom = Maps.newConcurrentMap();
        }
        Random random = groupRandom.get(groupConsumer.getGroup());
        if (random == null) {
            random = new Random();
            groupRandom.put(groupConsumer.getGroup(), random);
        }
        return random;
    }
}
