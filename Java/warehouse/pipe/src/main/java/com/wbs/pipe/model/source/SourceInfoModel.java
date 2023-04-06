package com.wbs.pipe.model.source;


import com.wbs.common.model.BaseStatusModel;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SourceInfoModel
 */
public class SourceInfoModel extends BaseStatusModel {
    private String name;
    private String table_name;
    private int type;
    private String desc;
    private String connect_id;

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

    public String getConnect_id() {
        return connect_id;
    }

    public void setConnect_id(String connect_id) {
        this.connect_id = connect_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
}
