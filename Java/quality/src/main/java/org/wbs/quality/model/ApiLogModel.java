package org.wbs.quality.model;

import java.util.Map;

/**
 * @author WBS
 * Date:2022/6/6
 */

public class ApiLogModel {
    public String Name;
    public Long Time;
    public Object Result;
    public String Client;
    public String Url;
    public String Method;
    public Map<String, Object> Parameters;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Long getTime() {
        return Time;
    }

    public void setTime(Long time) {
        Time = time;
    }

    public Object getResult() {
        return Result;
    }

    public void setResult(Object result) {
        Result = result;
    }

    public String getClient() {
        return Client;
    }

    public void setClient(String client) {
        Client = client;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public Map<String, Object> getParameters() {
        return Parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        Parameters = parameters;
    }

}