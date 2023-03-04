package com.wbs.common.database.model;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/4 17:41
 * @desciption ColumnInfo
 */
public class ColumnInfo implements Serializable {
    public String name;
    public String type;
    public String table;
    public String comment;
    public int primary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }
}
