package com.wbs.pipe.model;

import com.wbs.common.model.BaseStatusModel;

import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/9 10:51
 * @desciption ColumnConfigModel
 */
public class ColumnConfigModel extends BaseStatusModel {
    private String task_id;
    private Map<String, String> mapper;

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
}
