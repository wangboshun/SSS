package com.wbs.common.database.base.model;

/**
 * @author WBS
 * @date 2023/3/5 19:25
 * @desciption ResultSetColumnKeys
 */
public enum ResultEnum {
    /**
     * 表类别
     */
    TABLE_CAT,
    /**
     * 表模式
     */
    TABLE_SCHEM,
    /**
     * 表名称
     */
    TABLE_NAME,
    /**
     * 列名称
     */
    COLUMN_NAME,
    /**
     * 来自 java.sql.Types 的 SQL 类型
     */
    DATA_TYPE,
    /**
     * 数据源依赖的类型名称，对于 UDT，该类型名称是完全限定的
     */
    TYPE_NAME,
    /**
     * 列的大小
     */
    COLUMN_SIZE,
    /**
     * 小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 Null。
     */
    DECIMAL_DIGITS,
    /**
     * 基数（通常为 10 或 2）
     */
    NUM_PREC_RADIX,
    /**
     * 是否允许使用 NULL。 columnNoNulls - 可能不允许使用NULL值， columnNullable - 明确允许使用NULL值， columnNullableUnknown - 不知道是否可使用 null
     */
    NULLABLE,
    /**
     * 描述列的注释（可为null）
     */
    REMARKS,
    /**
     * 该列的默认值，当值在单引号内时应被解释为一个字符串（可为null）
     */
    COLUMN_DEF,
    /**
     * 未使用
     */
    SQL_DATA_TYPE,
    /**
     * 未使用
     */
    SQL_DATETIME_SUB,
    /**
     * 对于 char 类型，该长度是列中的最大字节数
     */
    CHAR_OCTET_LENGTH,
    /**
     * 表中的列的索引（从 1 开始）
     */
    ORDINAL_POSITION,
    /**
     * 是否允许使用 NULL， columnNoNulls - 可能不允许使用NULL值， columnNullable - 明确允许使用NULL值， columnNullableUnknown - 不知道是否可使用 null
     */
    IS_NULLABLE,
    /**
     * 表的类别，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为null）
     */
    SCOPE_CATLOG,
    /**
     * 表的模式，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为null）
     */
    SCOPE_SCHEMA,
    /**
     * 表名称，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为null）
     */
    SCOPE_TABLE,
    /**
     * 不同类型或用户生成 Ref 类型、来自 java.sql.Types 的 SQL 类型的源类型（如果 DATA_TYPE 不是 DISTINCT 或用户生成的 REF，则为null）
     */
    SOURCE_DATA_TYPE,
    /**
     * 指示此列是否自动增加，YES --- 如果该列自动增加 ， NO --- 如果该列不自动增加， 空字符串 --- 如果不能确定该列是否是自动增加参数
     */
    IS_AUTOINCREMENT;
}
