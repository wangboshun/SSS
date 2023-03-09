package com.wbs.pipe.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/9 10:51
 * @desciption ColumnConfigModel
 */
public class ColumnConfigModel implements Serializable {
    private String id;
    private String task_id;
    private Map<String, String> mapper;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public Map<String, String> getMapper() {
        return mapper;
    }

    public void setMapper(Map<String, String> mapper) {
        this.mapper = mapper;
    }

    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }

    public LocalDateTime getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(LocalDateTime update_time) {
        this.update_time = update_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
