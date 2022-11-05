package com.zny.pipe.component.enums;

/**
 * @author WBS
 * Date 2022-10-31 11:27
 * TaskStatus
 */

public enum TaskStatusEnum {

    /**
     * 无状态
     */
    NONE,

    /**
     * 已创建，未运行
     */
    CREATE,

    /**
     * 连接失败
     */
    CONNECT_FAIL,

    /**
     * 运行中
     */
    RUNNING,

    /**
     * 已完成
     */
    COMPLETE,

    /**
     * 已取消
     */
    CANCEL;
}
