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

    /**
     * 转换字段
     */
    private String convert_field;

    /**
     * 操作值
     */
    private String convert_value;

    /**
     * 计算值
     */
    private String convert_number;
    private String create_time;
    private Integer convert_status;

    /**
     * 操作符号
     * 加减符号:+、-、*、/
     * 如果是String类型，是这样：int+、int-、int*、int/
     */
    private String convert_symbol;


    /**
     * 转换顺序，按照从小到大的次序进行转换
     */
    private Integer convert_index;

    public Integer getConvert_index() {
        return convert_index;
    }

    public void setConvert_index(Integer convert_index) {
        this.convert_index = convert_index;
    }

    public String getConvert_symbol() {
        return convert_symbol;
    }

    public void setConvert_symbol(String convert_symbol) {
        this.convert_symbol = convert_symbol;
    }

    public String getConvert_number() {
        return convert_number;
    }

    public void setConvert_number(String convert_number) {
        this.convert_number = convert_number;
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

    public String getConvert_value() {
        return convert_value;
    }

    public void setConvert_value(String convert_value) {
        this.convert_value = convert_value;
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
