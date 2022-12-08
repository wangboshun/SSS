package com.zny.common.enums;


/**
 * @author WBS
 * Date:2022/10/20
 * 数据库类型枚举
 */

public enum DbTypeEnum {
    /**
     * MySql
     */
    MySql(0),

    /**
     * MsSql
     */
    MsSql(1),

    /**
     * PostgreSql
     */
    PostgreSql(2),

    /**
     * ClickHouse
     */
    ClickHouse(3);

    public Integer index;

    DbTypeEnum(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
