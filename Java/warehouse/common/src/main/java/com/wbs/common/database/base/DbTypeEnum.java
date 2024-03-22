package com.wbs.common.database.base;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption 数据库类型枚举
 */
public enum DbTypeEnum {

    /**
     * 未知
     */
    NONE,

    /**
     * MySql
     */
    MYSQL,

    /**
     * SqlServer
     */
    SQLSERVER,

    /**
     * PostgreSql
     */
    POSTGRESQL,

    /**
     * ClickHouse
     */
    CLICKHOUSE,

    /**
     * RabbitMQ
     */
    RABBITMQ,

    /**
     * Kafka
     */
    KAFKA
}
