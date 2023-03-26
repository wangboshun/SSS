package com.wbs.common.database.base.model;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/4 17:41
 * @desciption ColumnInfo
 */
public class ColumnInfo implements Serializable {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 数据类型
     */
    private String dbType;

    /**
     * 表名
     */
    private String table;

    /**
     * 备注
     */
    private String comment;

    /**
     * 主键，1代表主键
     */
    private int primary;

    /**
     * 对应java类型
     */
    private String javaType;

    /**
     * 可否为空
     */
    private int isNullable;

    /**
     * 小数点保留位
     */
    private int scale;

    /**
     * 数据长度
     */
    private int lenght;

    public int getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(int isNullable) {
        this.isNullable = isNullable;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
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

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }
}
