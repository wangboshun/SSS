package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date 2022-11-15 15:37
 * 目的配置类
 */

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
