package com.zny.pipe.model;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date 2022-11-05 12:01
 * MessageBodyModel
 */

public class MessageBodyModel {

    List<Map<String, Object>> data;
    String taskId;

    Integer count;

    Integer status;

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
