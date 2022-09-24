package com.zny.system.model.api;

/**
 * @author WBS
 * Date:2022/9/6
 */

public enum ApiStatusEnum {

    /**
     * 启用
     */
    ON(1),

    /**
     * 禁用
     */
    OFF(0),

    /**
     * 删除
     */
    DELETE(2);

    public Integer index;

    ApiStatusEnum(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
