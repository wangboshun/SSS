package com.zny.common.eventbus.core;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class EventRoute {

    //topic-group映射关系
    public static Map<String, CopyOnWriteArrayList<String>> topicGroup = Maps.newConcurrentMap();
    //group-consumer映射关系
    public static Map<String, CopyOnWriteArrayList<EventConsumer>> groupConsumer = Maps.newConcurrentMap();
}
