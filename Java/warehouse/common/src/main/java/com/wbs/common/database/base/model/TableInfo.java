package com.wbs.common.database.base.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/4 16:48
 * @desciption TableInfo
 */
@Setter
@Getter
public class TableInfo implements Serializable {

    /**
     * 表名
     */
    private String name;

    /**
     * 备注
     */
    private String comment;

    /**
     * 库名
     */
    private String db;

    private String schema;

    /**
     * 表类型
     */
    private String type;

    /**
     * 数据量
     */
    private int total;
}
