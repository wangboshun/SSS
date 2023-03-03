package com.wbs.engine.core.base;

import com.wbs.engine.model.DataTable;

import java.sql.Connection;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/2 15:29
 * @desciption IReader
 */
public interface IReader {

    public void config(String tableName, Connection connection);
    public void config(String tableName, Connection connection, Map<String, String> columns);

    public DataTable readData(String sql);
}
