package com.zny.common.eventbus.core;

/**
 *
 */
public interface Event {

    /**
     * 事件所属主题
     *
     * @return
     */
    String getTopic();

    /**
     * 事件内容
     *
     * @return
     */
    Object getContent();
}
