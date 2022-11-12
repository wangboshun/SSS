package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author WBS
 * Date 2022-11-12 13:53
 * FilterConfigModel
 */

@TableName("pipe_convert_config")
public class ConvertConfigModel {
    @TableId
    private String id;
    private String task_id;
    private String convert_field;
    private String convert_after;
    private String convert_before;
    private String create_time;
    private Integer convert_status;
    private String filter_symbol;
    private String convert_symbol;

    public String getConvert_symbol() {
        return convert_symbol;
    }

    public void setConvert_symbol(String convert_symbol) {
        this.convert_symbol = convert_symbol;
    }

    public String getFilter_symbol() {
        return filter_symbol;
    }

    public void setFilter_symbol(String filter_symbol) {
        this.filter_symbol = filter_symbol;
    }

    public String getConvert_before() {
        return convert_before;
    }

    public void setConvert_before(String convert_before) {
        this.convert_before = convert_before;
    }

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

    public String getConvert_field() {
        return convert_field;
    }

    public void setConvert_field(String convert_field) {
        this.convert_field = convert_field;
    }

    public String getConvert_after() {
        return convert_after;
    }

    public void setConvert_after(String convert_after) {
        this.convert_after = convert_after;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getConvert_status() {
        return convert_status;
    }

    public void setConvert_status(Integer convert_status) {
        this.convert_status = convert_status;
    }
}
