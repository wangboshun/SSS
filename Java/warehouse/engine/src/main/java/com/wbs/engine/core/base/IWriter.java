package com.wbs.engine.core.base;

import com.wbs.engine.model.DataRow;
import com.wbs.engine.model.DataTable;

import java.sql.Connection;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/2 15:29
 * @desciption IWriter
 */
public interface IWriter {
    public void config(String tableName, Connection connection);

    public void config(String tableName, Connection connection, Map<String, String> columns);

    public boolean writeData(DataTable dt);

    public boolean updateData(DataTable dt);
    boolean exists(DataRow row);
}
