package com.wbs.pipe.application.engine.pgsql;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.pipe.application.engine.base.db.DbReaderBase;

import java.sql.Connection;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/2 15:36
 * @desciption PgSqlReader
 */
public class PgSqlReader extends DbReaderBase {
    @Override
    public void config(String taskId, String tableName, Connection connection, List<ColumnInfo> columnList, List<WhereInfo> whereList) {
        this.dbType = DbTypeEnum.POSTGRESQL;
        super.config(taskId, tableName, connection, columnList, whereList);
    }
}
