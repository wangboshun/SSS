package com.wbs.pipe.application.engine.base;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.pipe.model.event.*;
import com.wbs.pipe.application.engine.clickhouse.ClickHouseReader;
import com.wbs.pipe.application.engine.clickhouse.ClickHouseWriter;
import com.wbs.pipe.application.engine.mysql.MySqlReader;
import com.wbs.pipe.application.engine.mysql.MySqlWriter;
import com.wbs.pipe.application.engine.pgsql.PgSqlReader;
import com.wbs.pipe.application.engine.pgsql.PgSqlWriter;
import com.wbs.pipe.application.engine.sqlserver.SqlServerReader;
import com.wbs.pipe.application.engine.sqlserver.SqlServerWriter;

/**
 * @author WBS
 * @date 2023/4/16 19:08
 * @desciption 管理类
 */
public class EngineManager {

    public static IWriter getWriter(DbTypeEnum dbType) {
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

    public static IReader getReader(DbTypeEnum dbType) {
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

    public static EventAbstractModel getEvent(DbTypeEnum dbType) {
        switch (dbType) {
            case MYSQL:
                return new MySqlEventModel();
            case SQLSERVER:
                return new SqlServerEventModel();
            case POSTGRESQL:
                return new PgSqlEventModel();
            case CLICKHOUSE:
                return new ClickHouseEventModel();
            default:
                return null;
        }
    }
}
