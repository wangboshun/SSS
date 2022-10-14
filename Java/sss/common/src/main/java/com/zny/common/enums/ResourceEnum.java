package com.zny.common.enums;

/**
 * @author WBS
 * Date:2022/9/6
 * 资源类型枚举
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
    API(4),

    /**
     * 测站
     */
    Station(5),

    /**
     * 传感器
     */
    Sensor(6);

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
