package com.wbs.pipe.application.engine.base.db;

import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.pipe.model.engine.InsertResult;
import com.wbs.pipe.model.engine.UpdateResult;

import java.sql.Connection;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/2 15:29
 * @desciption IWriter
 */
public interface IDbWriter {
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList);

    public InsertResult insertData(DataTable dt);

    public UpdateResult updateData(DataTable dt);
}
