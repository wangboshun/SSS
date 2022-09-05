package com.zny.common.eventbus.core;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.MoreExecutors;
import com.zny.common.eventbus.strategy.RandomStrategy;
import com.zny.common.eventbus.strategy.Strategy;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class EventBus {

    //事件总线标示
    private final String identifier;
    //事件订阅者执行线程池
    private final Executor executor;
    //事件订阅者执行错误处理器
    private final SubscriberExceptionHandler exceptionHandler;
    //事件订阅者注册器
    private final SubscriberRegistry registry = new SubscriberRegistry(this);
    //事件指派容器
    private final Dispatcher dispatcher;
    //事件订阅者调度策略
    private final Strategy strategy;

    /**
     * 默认事件总线构造器
     */
    public EventBus() {
        this("default");
    }

    /**
     * @param identifier
     */
    public EventBus(String identifier) {
        this(new RandomStrategy(), identifier, MoreExecutors.directExecutor(), new LogExceptionHandler());
    }

    /**
     * @param strategy
     * @param identifier
     */
    public EventBus(Strategy strategy, String identifier, Executor executor, SubscriberExceptionHandler exceptionHandler) {
        Preconditions.checkNotNull(strategy);
        Preconditions.checkArgument(StringUtils.isNotBlank(identifier), "identifier must not be empty");
        this.strategy = strategy;
        this.identifier = identifier;
        this.executor = executor;
        this.dispatcher = Dispatcher.immediateDispatch(strategy);
        this.exceptionHandler = exceptionHandler;
    }

    public Executor getExecutor() {
        return executor;
    }

    public SubscriberExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public SubscriberRegistry getRegistry() {
        return registry;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * 注册订阅者
     *
     * @param consumer 订阅者
     */
    public void register(Object consumer) {
        registry.register(consumer);
    }

    /**
     * 取消订阅者
     *
     * @param consumer 订阅者
     */
    public void unRegister(Object consumer) {
        registry.unregister(consumer);
    }

    /**
     * 发送订阅时间
     *
     * @param event 事件
     */
    public void post(Event event) {
        //只有存在订阅者的情况下，才进行事件指派
        if (registry.hasConsumers(event)) {
            Map<String, List<EventConsumer>> consumers = registry.getConsumers(event);
            dispatcher.dispatch(event, consumers);
        }
    }
}
