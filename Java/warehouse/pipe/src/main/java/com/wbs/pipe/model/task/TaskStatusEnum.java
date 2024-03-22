package com.wbs.pipe.model.task;

/**
 * @author WBS
 * @date 2023/4/7 16:29
 * @desciption TaskEnum
 */
public enum TaskStatusEnum {
    /**
     * 就绪
     */
    WAIT,

    /**
     * 运行中
     */
    RUNNING,

    /**
     * 取消
     */
    CANCEL,

    /**
     * 错误
     */
    ERROR,

    /**
     * 完成
     */
    COMPLETE
}
