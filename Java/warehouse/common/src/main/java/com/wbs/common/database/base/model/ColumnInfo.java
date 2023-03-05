package com.wbs.common.database.base.model;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/4 17:41
 * @desciption ColumnInfo
 */
public class ColumnInfo implements Serializable {
    private String name;
    private String dbType;
    private String table;
    private String comment;
    private int primary;
    private String javaType;
    private int isNullAble;
    private int point;

    public int getIsNullAble() {
        return isNullAble;
    }

    public void setIsNullAble(int isNullAble) {
        this.isNullAble = isNullAble;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

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
