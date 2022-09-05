package com.zny.common.eventbus.core;

import com.google.common.base.Preconditions;
import com.zny.common.eventbus.strategy.Strategy;

import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class Dispatcher {

    /**
     * 获取事件指派容器
     *
     * @param strategy 订阅者调度策略
     * @return
     */
    public static Dispatcher immediateDispatch(Strategy strategy) {
        return new ImmediateDispatcher(strategy);
    }

    abstract void dispatch(Event event, Map<String, List<EventConsumer>> groupConsumer);

    /**
     * 事件指派容器实现类
     */
    private static final class ImmediateDispatcher extends Dispatcher {

        private Strategy strategy;

        public ImmediateDispatcher() {
        }

        public ImmediateDispatcher(Strategy strategy) {
            this.strategy = strategy;
        }

        public Strategy getStrategy() {
            return strategy;
        }

        public void setStrategy(Strategy strategy) {
            this.strategy = strategy;
        }

        @Override
        void dispatch(Event event, Map<String, List<EventConsumer>> groupConsumer) {
            Preconditions.checkNotNull(event);
            for (Map.Entry<String, List<EventConsumer>> groupConsumerEntry : groupConsumer.entrySet()) {
                GroupConsumer consumer = new GroupConsumer(groupConsumerEntry.getKey(), groupConsumerEntry.getValue());
                EventConsumer eventConsumer = strategy.select(consumer);
                eventConsumer.consume(event);
            }
        }
    }

}
