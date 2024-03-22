package com.wbs.pipe.application.engine.base.db;

/**
 * @author WBS
 * @date 2023/4/10 16:28
 * @desciption WriteEnum
 */
public enum DbWriteTypeEnum {
    /**
     * 插入更新
     */
    UPSERT,

    /**
     * 忽略跳过
     */
    IGNORE
}
