package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date 2022-11-15 15:37
 * 源配置类
 */

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
    private String primary_column;

    /**
     * 数据时间字段
     */
    private String time_column;

    /**
     * 写入时间字段
     */
    private String wrtm_column;

    /**
     * 排序类型
     * 0为asc、1为desc
     */
    private Integer order_type;

    /**
     * 排序字段
     */
    private String order_column;

    /**
     * 获取数据的方式
     * 0为按wrtm获取、1为按数据时间获取
     */
    private Integer get_type;


    public String getPrimary_column() {
        return primary_column;
    }

    public void setPrimary_column(String primary_column) {
        this.primary_column = primary_column;
    }

    public String getTime_column() {
        return time_column;
    }

    public void setTime_column(String time_column) {
        this.time_column = time_column;
    }

    public String getWrtm_column() {
        return wrtm_column;
    }

    public void setWrtm_column(String wrtm_column) {
        this.wrtm_column = wrtm_column;
    }

    public Integer getOrder_type() {
        return order_type;
    }

    public void setOrder_type(Integer order_type) {
        this.order_type = order_type;
    }

    public String getOrder_column() {
        return order_column;
    }

    public void setOrder_column(String order_column) {
        this.order_column = order_column;
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
