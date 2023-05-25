package com.wbs.pipe.application.engine.base;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.enums.MQTypeEnum;
import com.wbs.pipe.application.engine.base.db.IDbReader;
import com.wbs.pipe.application.engine.base.db.IDbWriter;
import com.wbs.pipe.application.engine.base.mq.IMQSender;
import com.wbs.pipe.application.engine.clickhouse.ClickHouseReader;
import com.wbs.pipe.application.engine.clickhouse.ClickHouseWriter;
import com.wbs.pipe.application.engine.kafka.KafkaMQSender;
import com.wbs.pipe.application.engine.mysql.MySqlReader;
import com.wbs.pipe.application.engine.mysql.MySqlWriter;
import com.wbs.pipe.application.engine.pgsql.PgSqlReader;
import com.wbs.pipe.application.engine.pgsql.PgSqlWriter;
import com.wbs.pipe.application.engine.rabbitmq.RabbitMQSender;
import com.wbs.pipe.application.engine.sqlserver.SqlServerReader;
import com.wbs.pipe.application.engine.sqlserver.SqlServerWriter;

/**
 * @author WBS
 * @date 2023/4/16 19:08
 * @desciption 管理类
 */
public class EngineManager {

    public static IDbWriter getWriter(DbTypeEnum dbType) {
        switch (dbType) {
            case MYSQL:
                return new MySqlWriter();
            case SQLSERVER:
                return new SqlServerWriter();
            case CLICKHOUSE:
                return new ClickHouseWriter();
            case POSTGRESQL:
                return new PgSqlWriter();
            default:
                return null;
        }
    }

    public static IDbReader getReader(DbTypeEnum dbType) {
        switch (dbType) {
            case MYSQL:
                return new MySqlReader();
            case SQLSERVER:
                return new SqlServerReader();
            case CLICKHOUSE:
                return new ClickHouseReader();
            case POSTGRESQL:
                return new PgSqlReader();
            default:
                return null;
        }
    }

    public static IMQSender getSender(MQTypeEnum mqType) {
        switch (mqType) {
            case RABBITMQ:
                return new RabbitMQSender();
            case KAFKA:
                return new KafkaMQSender();
            default:
                return null;
        }
    }
}
