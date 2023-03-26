package com.wbs.common.database.base.model;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/4 16:48
 * @desciption TableInfo
 */
public class TableInfo implements Serializable {

    /**
     * 表名
     */
    private String name;

    /**
     * 备注
     */
    private String comment;

    /**
     * 库名
     */
    private String db;

    /**
     * 表类型
     */
    private String type;

    /**
     * 数据量
     */
    private int total;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
