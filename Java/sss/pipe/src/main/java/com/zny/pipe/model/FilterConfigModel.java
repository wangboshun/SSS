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

    /**
     * 筛选字段
     */
    private String filter_column;

    /**
     * 筛选符号
     * 符号:==、!=、>、<、>=、<=、contains
     * 如果是String类型，是这样：int==、int>、int<
     */
    private String filter_symbol;

    /**
     * 筛选值
     */
    private String filter_value;
    private String create_time;
    private Integer filter_status;

    /**
     * 筛选类型
     * AND、OR
     */
    private String filter_type;

    /**
     * 使用场景类型
     * 0为数据筛选、1为转换筛选
     */
    private Integer use_type;

    public Integer getUse_type() {
        return use_type;
    }

    public void setUse_type(Integer use_type) {
        this.use_type = use_type;
    }

    public String getFilter_type() {
        return filter_type;
    }

    public void setFilter_type(String filter_type) {
        this.filter_type = filter_type;
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

    public String getFilter_column() {
        return filter_column;
    }

    public void setFilter_column(String filter_column) {
        this.filter_column = filter_column;
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
