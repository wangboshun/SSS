package com.zny.system.model.apilog;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/2
 */

@TableName("sys_api_log")
public class ApiLogModel implements Serializable {

    public String id;
    public String user_id;
    public Float spend;
    public String url;
    public String method;
    public String params;
    public String ip;
    public Integer code;
    public String data;
    public String start_time;
    public String end_time;

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
        return "ApiLogModel{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", spend=" + spend +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", ip='" + ip + '\'' +
                ", code=" + code +
                ", data='" + data + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                '}';
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
