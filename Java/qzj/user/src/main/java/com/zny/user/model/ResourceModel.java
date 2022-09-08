package com.zny.user.model;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/6
 */

@TableName("sys_resource")
public class ResourceModel implements Serializable {

    public String id;

    public String main_id;
    public String main_name;
    public int main_type;

    public String slave_id;
    public String slave_name;
    public int slave_type;
    public String slave_code;
    public String create_time;
    public Integer resource_status;

    public String getSlave_code() {
        return slave_code;
    }

    public void setSlave_code(String slave_code) {
        this.slave_code = slave_code;
    }

    public int getSlave_type() {
        return slave_type;
    }

    public void setSlave_type(int slave_type) {
        this.slave_type = slave_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain_id() {
        return main_id;
    }

    public void setMain_id(String main_id) {
        this.main_id = main_id;
    }

    public String getMain_name() {
        return main_name;
    }

    public void setMain_name(String main_name) {
        this.main_name = main_name;
    }

    public String getSlave_id() {
        return slave_id;
    }

    public void setSlave_id(String slave_id) {
        this.slave_id = slave_id;
    }

    public String getSlave_name() {
        return slave_name;
    }

    public void setSlave_name(String slave_name) {
        this.slave_name = slave_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getResource_status() {
        return resource_status;
    }

    public void setResource_status(Integer resource_status) {
        this.resource_status = resource_status;
    }

    public int getMain_type() {
        return main_type;
    }

    public void setMain_type(int main_type) {
        this.main_type = main_type;
    }
}
