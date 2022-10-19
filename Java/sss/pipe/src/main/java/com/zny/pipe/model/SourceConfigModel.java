package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("pipe_source_config")
public class SourceConfigModel implements Serializable {

    @TableId
    private String id;
    private String source_name;
    private String connect_id;
    private String create_time;
    private Integer source_status;

    /**
     * 表名
     */
    private String table_name;

    /**
     * 主键，以逗号分割
     */
    private String primary_field;

    /**
     * 数据时间字段
     */
    private String time_field;

    /**
     * 写入时间字段
     */
    private String wrtm_field;

    /**
     * 排序类型
     * 0为asc、1为desc
     */
    private Integer order_type;

    /**
     * 排序字段
     */
    private String order_field;

    /**
     * 获取数据的方式
     * 0为按wrtm获取、1为按数据时间获取
     */
    private Integer get_type;


    public String getPrimary_field() {
        return primary_field;
    }

    public void setPrimary_field(String primary_field) {
        this.primary_field = primary_field;
    }

    public String getTime_field() {
        return time_field;
    }

    public void setTime_field(String time_field) {
        this.time_field = time_field;
    }

    public String getWrtm_field() {
        return wrtm_field;
    }

    public void setWrtm_field(String wrtm_field) {
        this.wrtm_field = wrtm_field;
    }

    public Integer getOrder_type() {
        return order_type;
    }

    public void setOrder_type(Integer order_type) {
        this.order_type = order_type;
    }

    public String getOrder_field() {
        return order_field;
    }

    public void setOrder_field(String order_field) {
        this.order_field = order_field;
    }

    public Integer getGet_type() {
        return get_type;
    }

    public void setGet_type(Integer get_type) {
        this.get_type = get_type;
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

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
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

    public Integer getSource_status() {
        return source_status;
    }

    public void setSource_status(Integer source_status) {
        this.source_status = source_status;
    }
}
