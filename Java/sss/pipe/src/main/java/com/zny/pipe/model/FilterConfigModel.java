package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author WBS
 * Date 2022-11-12 13:53
 * FilterConfigModel
 */

@TableName("pipe_filter_config")
public class FilterConfigModel {
    @TableId
    private String id;
    private String task_id;
    private String filter_field;
    private String filter_symbol;
    private String filter_value;
    private String create_time;
    private Integer filter_status;

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

    public String getFilter_field() {
        return filter_field;
    }

    public void setFilter_field(String filter_field) {
        this.filter_field = filter_field;
    }

    public String getFilter_symbol() {
        return filter_symbol;
    }

    public void setFilter_symbol(String filter_symbol) {
        this.filter_symbol = filter_symbol;
    }

    public String getFilter_value() {
        return filter_value;
    }

    public void setFilter_value(String filter_value) {
        this.filter_value = filter_value;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getFilter_status() {
        return filter_status;
    }

    public void setFilter_status(Integer filter_status) {
        this.filter_status = filter_status;
    }
}
