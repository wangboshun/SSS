package com.zny.common.utils.database;

import java.io.Serializable;

/**
 * 字段信息
 */
public class TableInfo implements Serializable {
    private String column_name;
    private String java_type;
    private Integer is_primary;
    private Integer is_null;
    private String db_type;

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getJava_type() {
        return java_type;
    }

    public void setJava_type(String java_type) {
        this.java_type = java_type;
    }

    public Integer getIs_primary() {
        return is_primary;
    }

    public void setIs_primary(Integer is_primary) {
        this.is_primary = is_primary;
    }

    public Integer getIs_null() {
        return is_null;
    }

    public void setIs_null(Integer is_null) {
        this.is_null = is_null;
    }

    public String getDb_type() {
        return db_type;
    }

    public void setDb_type(String db_type) {
        this.db_type = db_type;
    }
}
