package com.zny.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/9
 */

@TableName("sys_api")
@JsonIgnoreProperties(value = {"api_path"})  //不返回path字段到前端
public class ApiModel implements Serializable {
    public String id;
    public String api_name;
    public String api_code;
    public String api_type;
    public Integer api_status;
    public String create_time;
    public String api_path;
    public String api_group;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApi_group() {
        return api_group;
    }

    public void setApi_group(String api_group) {
        this.api_group = api_group;
    }

    public Integer getApi_status() {
        return api_status;
    }

    public void setApi_status(Integer api_status) {
        this.api_status = api_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getApi_path() {
        return api_path;
    }

    public void setApi_path(String api_path) {
        this.api_path = api_path;
    }

    public String getApi_name() {
        return api_name;
    }

    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    public String getApi_code() {
        return api_code;
    }

    public void setApi_code(String api_code) {
        this.api_code = api_code;
    }

    public String getApi_type() {
        return api_type;
    }

    public void setApi_type(String api_type) {
        this.api_type = api_type;
    }
}
