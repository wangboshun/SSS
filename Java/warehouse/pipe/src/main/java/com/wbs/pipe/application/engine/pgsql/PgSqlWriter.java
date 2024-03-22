package com.wbs.pipe.application.engine.pgsql;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.pipe.application.engine.base.db.DbWriterBase;

import java.sql.Connection;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption PgSqlWriter
 */
public class PgSqlWriter extends DbWriterBase {
    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList) {
        this.dbType = DbTypeEnum.POSTGRESQL;
        super.config(tableName, connection, columnList);
    }
}
