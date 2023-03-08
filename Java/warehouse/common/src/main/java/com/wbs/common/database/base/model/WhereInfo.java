package com.wbs.common.database.base.model;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/7 14:26
 * @desciption WhereInfo
 */
public class WhereInfo implements Serializable {

    /**
     * 字段
     */
    private String column;

    /**
     * 运算符：=、>、<、IN、LIKE、IS NULL
     */
    private String symbol;

    /**
     * 值
     */
    private Object value;

    /**
     * 操作符：AND、OR
     */
    private String operate;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
