package com.zny.common.enums;


/**
 * @author WBS
 * Date:2022/10/20
 * 数据库类型枚举
 */

public enum DbTypeEnum {
    /**
     * MySQL
     */
    MySQL(0),

    /**
     * MsSQL
     */
    MsSQL(1);

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
