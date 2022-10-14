package com.zny.system.model.apilog;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/2
 * api日志类
 */

@TableName("sys_api_log")
public class ApiLogModel implements Serializable {

    private String id;
    private String user_id;
    private Float spend;
    private String url;
    private String method;
    private String params;
    private String api_name;
    private String ip;
    private Integer code;
    private String data;
    private String start_time;
    private String end_time;

    public String getApi_name() {
        return api_name;
    }

    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "ApiLogModel{" + "id='" + id + '\'' + ", user_id='" + user_id + '\'' + ", spend=" + spend + ", url='" + url + '\'' + ", method='" + method + '\'' + ", params='" + params + '\'' + ", api_name='" + api_name + '\'' + ", ip='" + ip + '\'' + ", code=" + code + ", data='" + data + '\'' + ", start_time='" + start_time + '\'' + ", end_time='" + end_time + '\'' + '}';
    }

    public Float getSpend() {
        return spend;
    }

    public void setSpend(Float spend) {
        this.spend = spend;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
