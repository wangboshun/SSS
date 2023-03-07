package com.wbs.engine.core.base;

import com.wbs.common.database.base.DataTable;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.WhereInfo;

import java.sql.Connection;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/2 15:29
 * @desciption IReader
 */
public interface IReader {

    public void config(String tableName, Connection connection);

    public void config(String tableName, Connection connection, List<ColumnInfo> columnList);

    public DataTable readData(List<WhereInfo> whereList);

    public DataTable readData(String sql);
}
