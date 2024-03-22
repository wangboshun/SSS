package com.wbs.common.database.base.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/7 14:26
 * @desciption WhereInfo
 */
@Setter
@Getter
public class WhereInfo implements Serializable {

    /**
     * 字段
     */
    private String column;

    /**
     * 运算符：=、>、<、IN、LIKE、IS NULL
     */
    private String symbol;

    /**
     * 值
     */
    private Object value;

    /**
     * 操作符：AND、OR
     */
    private String operate;
}
