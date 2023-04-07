package com.wbs.pipe.model.task;

import com.wbs.common.extend.BaseStatusModel;

/**
 * @author WBS
 * @date 2023/3/9 11:04
 * @desciption TaskInfoModel
 */
public class TaskInfoModel extends BaseStatusModel {
    private String name;
    private int type;
    private String desc;
    private String sink_id;
    private String source_id;
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
}
