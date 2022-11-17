package com.zny.pipe.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date 2022-11-05 12:01
 * mq消息体类
 */

public class MessageBodyModel implements Serializable {

    List<Map<String, Object>> data;
    String taskId;

    Integer total;

    Integer status;

    Integer current;

    Integer batch_size;

    Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getBatch_size() {
        return batch_size;
    }

    public void setBatch_size(Integer batch_size) {
        this.batch_size = batch_size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
