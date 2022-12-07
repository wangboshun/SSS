package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zny.common.utils.database.TableInfo;

/**
 * @author WBS
 * Date 2022-11-14 15:43
 * 表信息配置类
 */

@TableName("pipe_table_config")
public class TableConfigModel extends TableInfo {
    @TableId
    private String id;
    private String connect_id;
    private String create_time;
    private Integer table_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnect_id() {
        return connect_id;
    }

    public void setConnect_id(String connect_id) {
        this.connect_id = connect_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getTable_status() {
        return table_status;
    }

    public void setTable_status(Integer table_status) {
        this.table_status = table_status;
    }
}
