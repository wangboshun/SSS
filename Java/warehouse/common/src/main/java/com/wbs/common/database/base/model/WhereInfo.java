package com.wbs.common.database.base.model;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/7 14:26
 * @desciption WhereInfo
 */
public class WhereInfo implements Serializable {
    private String column;
    private String operate;
    private Object value;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
