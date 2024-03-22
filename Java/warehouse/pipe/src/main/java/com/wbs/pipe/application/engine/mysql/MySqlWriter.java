package com.wbs.pipe.application.engine.mysql;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.pipe.application.engine.base.db.DbWriterBase;

import java.sql.Connection;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption MySqlWriter
 */
public class MySqlWriter extends DbWriterBase {
    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList) {
        this.dbType = DbTypeEnum.MYSQL;
        super.config(tableName, connection, columnList);
    }
}
