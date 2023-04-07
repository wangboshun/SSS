package com.wbs.pipe.model.task;

import com.wbs.engine.model.WriterResult;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/4/7 15:54
 * @desciption TaskLogModel
 */
public class TaskLogModel implements Serializable {
    private String id;
    private String task_id;
    private String status;
    private WriterResult insert;
    private WriterResult update;
    private LocalDateTime ct;
    private LocalDateTime st;
    private LocalDateTime et;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSt() {
        return st;
    }

    public void setSt(LocalDateTime st) {
        this.st = st;
    }

    public LocalDateTime getEt() {
        return et;
    }

    public void setEt(LocalDateTime et) {
        this.et = et;
    }

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

    public WriterResult getInsert() {
        return insert;
    }

    public void setInsert(WriterResult insert) {
        this.insert = insert;
    }

    public WriterResult getUpdate() {
        return update;
    }

    public void setUpdate(WriterResult update) {
        this.update = update;
    }

    public LocalDateTime getCt() {
        return ct;
    }

    public void setCt(LocalDateTime ct) {
        this.ct = ct;
    }
}
