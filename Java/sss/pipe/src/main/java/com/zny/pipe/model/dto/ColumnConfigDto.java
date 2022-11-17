package com.zny.pipe.model.dto;

/**
 * @author WBS
 * Date 2022-11-15 16:51
 * 字段DTO
 */

public class ColumnConfigDto {

    private String task_id;

    private String sink_column;

    private String source_column;

    private String default_value;

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
}
