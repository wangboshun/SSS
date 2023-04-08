package com.wbs.common.database.base.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/4 17:41
 * @desciption ColumnInfo
 */
@Setter
@Getter
public class ColumnInfo implements Serializable {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 数据类型
     */
    private String dbType;

    /**
     * 表名
     */
    private String table;

    /**
     * 备注
     */
    private String comment;

    /**
     * 主键，1代表主键
     */
    private int primary;

    /**
     * 对应java类型
     */
    private String javaType;

    /**
     * 可否为空
     */
    private int isNullable;

    /**
     * 小数点保留位
     */
    private int scale;

    /**
     * 数据长度
     */
    private int lenght;
}
