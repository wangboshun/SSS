package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date 2022-11-15 15:37
 * ColumnConfigModel
 */

@TableName("pipe_column_config")
public class ColumnConfigModel implements Serializable {

    @TableId
    private String id;

    private String task_id;

    private String sink_column;

    private String source_column;

    private String default_value;

    private Integer column_status;

    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getSink_column() {
        return sink_column;
    }

    public void setSink_column(String sink_column) {
        this.sink_column = sink_column;
    }

    public String getSource_column() {
        return source_column;
    }

    public void setSource_column(String source_column) {
        this.source_column = source_column;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

    public Integer getColumn_status() {
        return column_status;
    }

    public void setColumn_status(Integer column_status) {
        this.column_status = column_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
