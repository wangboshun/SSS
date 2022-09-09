package com.zny.user.model;

/**
 * @author WBS
 * Date:2022/9/6
 */

public enum ResourceEnum {


    /**
     * 角色
     */
    ROLE(0),

    /**
     * 用户
     */
    USER(1),
    /**
     * 菜单
     */
    MENU(2),

    /**
     * 权限
     */
    PERMISSION(3),

    /**
     * API
     */
    API(4);

    public Integer index;

    ResourceEnum(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
