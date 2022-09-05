package com.zny.common.eventbus.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class SubscriberRegistry {

    private final EventBus bus;

    public SubscriberRegistry(EventBus bus) {
        this.bus = bus;
    }

    public EventBus getBus() {
        return bus;
    }


    /**
     * 注册事件订阅者
     *
     * @param consumer 事件订阅者
     */
    public void register(Object consumer) {
        EventConsumerMap eventConsumerMap = findConsumerMap(consumer);
        //注册topic-group映射关系
        for (Map.Entry<String, List<String>> topicGroupEntry : eventConsumerMap.getTopicGroup().entrySet()) {
            String topic = topicGroupEntry.getKey();
            CopyOnWriteArrayList<String> groupList = EventRoute.topicGroup.get(topic);
            if (groupList == null) {
                groupList = Lists.newCopyOnWriteArrayList();
                EventRoute.topicGroup.put(topic, groupList);
            }
            groupList.addAll(topicGroupEntry.getValue());
        }
        //注册group-consumer映射关系
        for (Map.Entry<String, List<EventConsumer>> groupConsumerEntry : eventConsumerMap.getGroupConsumer().entrySet()) {
            String group = groupConsumerEntry.getKey();
            CopyOnWriteArrayList<EventConsumer> consumerList = EventRoute.groupConsumer.get(group);
            if (consumerList == null) {
                consumerList = Lists.newCopyOnWriteArrayList();
                EventRoute.groupConsumer.put(group, consumerList);
            }
            consumerList.addAll(groupConsumerEntry.getValue());
        }
    }

    /**
     * 取消事件订阅者
     * 主要两方面：1.删除group-consumer映射关系；
     * 2.如果group-consumer映射关系为空，删除topic-group映射关系
     *
     * @param consumer 事件订阅者
     */
    public void unregister(Object consumer) {
        EventConsumerMap eventConsumerMap = findConsumerMap(consumer);
        //1.先删除group-consumer映射关系
        for (Map.Entry<String, List<EventConsumer>> groupConsumerEntry : eventConsumerMap.getGroupConsumer().entrySet()) {
            CopyOnWriteArrayList<EventConsumer> eventConsumers = EventRoute.groupConsumer.get(groupConsumerEntry.getKey());
            eventConsumers.removeAll(groupConsumerEntry.getValue());
        }
        //2.删除topic-group映射关系
        for (Map.Entry<String, List<String>> topicGroupEntry : eventConsumerMap.getTopicGroup().entrySet()) {
            CopyOnWriteArrayList<String> groupList = EventRoute.topicGroup.get(topicGroupEntry.getKey());
            for (String group : groupList) {
                //如果group-consumer映射关系为空，删除topic-group映射关系
                if (EventRoute.groupConsumer.get(group).size() == 0) {
                    groupList.remove(group);
                }
            }
        }
    }

    /**
     * 获取订阅事件的订阅者
     *
     * @param event 事件
     * @return 订阅者集合
     */
    public Map<String, List<EventConsumer>> getConsumers(Event event) {
        Map<String, List<EventConsumer>> groupConsumerMap = Maps.newHashMap();
        CopyOnWriteArrayList<String> groupList = EventRoute.topicGroup.get(event.getTopic());
        for (String group : groupList) {
            CopyOnWriteArrayList<EventConsumer> eventConsumers = EventRoute.groupConsumer.get(group);
            groupConsumerMap.put(group, Lists.newArrayList(eventConsumers));
        }
        return groupConsumerMap;
    }

    /**
     * 事件是否有对应的订阅者
     *
     * @param event 事件
     * @return
     */
    public boolean hasConsumers(Event event) {
        CopyOnWriteArrayList<String> groups = EventRoute.topicGroup.get(event.getTopic());
        //如果主题没有对应的分组，肯定没有订阅者
        if (groups == null || groups.size() == 0) {
            return false;
        }
        //如果有一个分组有事件订阅者，说明主题肯定有事件订阅者
        CopyOnWriteArrayList<EventConsumer> eventConsumers = null;
        for (String group : groups) {
            eventConsumers = EventRoute.groupConsumer.get(group);
            if (eventConsumers != null && eventConsumers.size() != 0) {
                return true;
            }
        }
        //没有事件订阅者
        return false;
    }

    /**
     * 获取事件消费者对象的全部事件处理关系
     *
     * @param consumer 事件消费者
     * @return
     */
    private EventConsumerMap findConsumerMap(Object consumer) {
        Preconditions.checkNotNull(consumer);
        Class<?> clazz = consumer.getClass();
        //topic-group映射关系
        Map<String, List<String>> topicGroup = Maps.newHashMap();
        //group-consumer映射关系
        Map<String, List<EventConsumer>> groupConsumer = Maps.newHashMap();
        //事件订阅者的方法进行处理
        for (Method method : clazz.getDeclaredMethods()) {
            Class<?>[] types = method.getParameterTypes();
            //参数的个数必须1并且参数的类型Event的子类
            if (types.length != 1 || !Event.class.isAssignableFrom(types[0])) {
                continue;
            }
            Listener annotation = method.getAnnotation(Listener.class);
            //如果方法上标注有@Listener注解
            if (annotation != null) {
                String group = annotation.group();
                String[] topics = annotation.topic();
                //如果group和topic都不允许为空
                if (StringUtils.isNotBlank(group) && (topics != null && topics.length > 0)) {
                    //处理topic-group映射关系
                    for (String topic : topics) {
                        List<String> groupList = topicGroup.get(topic);
                        if (groupList == null) {
                            groupList = Lists.newArrayList();
                            topicGroup.put(topic, groupList);
                        }
                        groupList.add(group);
                    }
                    //处理group-consumer映射关系
                    EventConsumer eventConsumer = new EventConsumer(bus, consumer, method);
                    List<EventConsumer> eventConsumerList = groupConsumer.get(group);
                    if (eventConsumerList == null) {
                        eventConsumerList = Lists.newArrayList();
                        groupConsumer.put(group, eventConsumerList);
                    }
                    eventConsumerList.add(eventConsumer);
                }
            }
        }
        return new EventConsumerMap(topicGroup, groupConsumer);
    }


    /**
     * 映射关系对象
     */
    public static class EventConsumerMap {

        private Map<String, List<String>> topicGroup;

        private Map<String, List<EventConsumer>> groupConsumer;

        public EventConsumerMap() {
        }

        public EventConsumerMap(Map<String, List<String>> topicGroup, Map<String
                , List<EventConsumer>> groupConsumer) {
            this.topicGroup = topicGroup;
            this.groupConsumer = groupConsumer;
        }

        public Map<String, List<String>> getTopicGroup() {
            return topicGroup;
        }

        public void setTopicGroup(Map<String, List<String>> topicGroup) {
            this.topicGroup = topicGroup;
        }

        public Map<String, List<EventConsumer>> getGroupConsumer() {
            return groupConsumer;
        }

        public void setGroupConsumer(Map<String, List<EventConsumer>> groupConsumer) {
            this.groupConsumer = groupConsumer;
        }
    }
}
