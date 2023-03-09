package com.wbs.pipe.model.task;

import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/3/9 11:04
 * @desciption TaskInfoModel
 */
public class TaskInfoModel {
    private String id;
    private String name;
    private int type;
    private int status;
    private String desc;
    private String sink_id;
    private String source_id;
    private LocalDateTime create_time;
    private LocalDateTime update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSink_id() {
        return sink_id;
    }

    public void setSink_id(String sink_id) {
        this.sink_id = sink_id;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
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
}
