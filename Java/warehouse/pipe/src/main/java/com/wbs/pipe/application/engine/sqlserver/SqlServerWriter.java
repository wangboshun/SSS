package com.wbs.pipe.application.engine.sqlserver;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.pipe.application.engine.base.WriterAbstract;

import java.sql.Connection;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption SqlServerWriter
 */
public class SqlServerWriter extends WriterAbstract {
    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList) {
        this.dbType = DbTypeEnum.SQLSERVER;
        super.config(tableName, connection, columnList);
    }
}
