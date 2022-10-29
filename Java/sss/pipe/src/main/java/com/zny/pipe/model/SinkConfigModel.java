package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("pipe_sink_config")
public class SinkConfigModel implements Serializable {

    @TableId
    private String id;
    private String sink_name;
    private String connect_id;
    private String create_time;
    private Integer sink_status;

    /**
     * 表名
     */
    private String table_name;

    /**
     * 主键，以逗号分割
     */
    private String primary_field;

    public String getPrimary_field() {
        return primary_field;
    }

    public void setPrimary_field(String primary_field) {
        this.primary_field = primary_field;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSink_name() {
        return sink_name;
    }

    public void setSink_name(String sink_name) {
        this.sink_name = sink_name;
    }

    public String getConnect_id() {
        return connect_id;
    }

    public void setConnect_id(String connect_id) {
        this.connect_id = connect_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getSink_status() {
        return sink_status;
    }

    public void setSink_status(Integer sink_status) {
        this.sink_status = sink_status;
    }
}
