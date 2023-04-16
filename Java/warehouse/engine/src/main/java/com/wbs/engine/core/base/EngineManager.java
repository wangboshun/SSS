package com.wbs.engine.core.base;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.engine.core.clickhouse.ClickHouseReader;
import com.wbs.engine.core.clickhouse.ClickHouseWriter;
import com.wbs.engine.core.mysql.MySqlReader;
import com.wbs.engine.core.mysql.MySqlWriter;
import com.wbs.engine.core.pgsql.PgSqlReader;
import com.wbs.engine.core.pgsql.PgSqlWriter;
import com.wbs.engine.core.sqlserver.SqlServerReader;
import com.wbs.engine.core.sqlserver.SqlServerWriter;

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
}
