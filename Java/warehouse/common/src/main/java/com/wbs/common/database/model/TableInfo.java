package com.wbs.common.database.model;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/4 16:48
 * @desciption TableInfo
 */
public class TableInfo implements Serializable {
    public String name;
    public String comment;
    public String db;
    private int total;

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
