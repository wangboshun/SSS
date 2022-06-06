package org.wbs.quality.model;

import java.util.Map;

/**
 * @author WBS
 * Date:2022/6/6
 */

public class MethodLogModel {
    private String Name;
    private Long Time;
    private Object Result;
    private Map<String, Object> Parameters;
    private String attribute;

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

    public Map<String, Object> getParameters() {
        return Parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        Parameters = parameters;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}