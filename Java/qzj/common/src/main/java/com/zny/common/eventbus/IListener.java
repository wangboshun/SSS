package com.zny.common.eventbus;

/**
 * @author WBS
 * Date:2022/9/5
 */

public interface IListener<T> {
    void register();

    void receive(T event);
}
