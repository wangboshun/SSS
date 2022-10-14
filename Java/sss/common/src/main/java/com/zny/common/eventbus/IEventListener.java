package com.zny.common.eventbus;

/**
 * @author WBS
 * Date:2022/9/5
 * eventbus监听接口
 */

public interface IEventListener<T> {
    void register();

    void receive(T event);
}
