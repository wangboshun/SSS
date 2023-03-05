package com.wbs.common.database.base.model;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/4 17:41
 * @desciption ColumnInfo
 */
public class ColumnInfo implements Serializable {
    public String name;
    public String dbType;
    public String table;
    public String comment;
    public int primary;
    private String javaType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
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

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}
