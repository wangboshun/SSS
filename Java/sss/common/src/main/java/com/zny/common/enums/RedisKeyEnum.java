package com.zny.common.enums;

/**
 * @author WBS
 * Date 2022-10-28 15:29
 * RedisKey枚举类
 */

public enum RedisKeyEnum {

    /**
     * 任务同步时间Key
     */
    TASK_TIME_CACHE,

    /**
     * Sink状态日志key
     */
    SINK_TIME_CACHE,

    /**
     * Source状态日志key
     */
    SOURCE_TIME_CACHE,

    /**
     * 任务次数key
     */
    TASK_COUNT_CACHE;
}
