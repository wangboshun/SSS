package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date 2022-11-14 15:43
 * 表信息配置类
 */

@TableName("pipe_table_config")
public class TableConfigModel implements Serializable {
    @TableId
    private String id;
    private String connect_id;
    private String table_name;
    private String column_name;
    private String data_type;
    private Integer is_primary;
    private Integer is_null;
    private String jdbc_type;
    private String create_time;
    private Integer table_status;

    public String getJdbc_type() {
        return jdbc_type;
    }

    public void setJdbc_type(String jdbc_type) {
        this.jdbc_type = jdbc_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnect_id() {
        return connect_id;
    }

    public void setConnect_id(String connect_id) {
        this.connect_id = connect_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getTable_status() {
        return table_status;
    }

    public void setTable_status(Integer table_status) {
        this.table_status = table_status;
    }
}
