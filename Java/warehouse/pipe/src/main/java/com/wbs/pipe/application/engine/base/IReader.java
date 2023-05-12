package com.wbs.pipe.application.engine.base;

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

    public void config(String taskId, String tableName, Connection connection, List<ColumnInfo> columnList, List<WhereInfo> whereList);

    public void readData();

    public void readData(String sql);
}
