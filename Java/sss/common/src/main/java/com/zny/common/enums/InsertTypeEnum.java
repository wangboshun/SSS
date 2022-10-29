package com.zny.common.enums;

public enum InsertTypeEnum {

    /**
     * 存在跳过
     */
    IGNORE(0),

    /**
     * 存在更新
     */
    UPDATE(1);

    public Integer index;

    InsertTypeEnum(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
