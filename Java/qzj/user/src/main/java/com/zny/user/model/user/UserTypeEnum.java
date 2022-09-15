package com.zny.user.model.user;

/**
 * @author WBS
 * Date:2022/9/15
 */

public enum UserTypeEnum {

    /**
     * 普通用户，可定制权限
     */
    COMMON(0),

    /**
     * 超级用户，拥有所有权限
     */
    SUPER(1),

    /**
     * 只读用户，只能查看数据
     */
    READ(2);

    public Integer index;

    UserTypeEnum(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

}
