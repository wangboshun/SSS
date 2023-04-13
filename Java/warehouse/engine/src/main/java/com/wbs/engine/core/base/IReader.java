package com.wbs.engine.core.base;

import com.wbs.common.database.base.DataTable;

/**
 * @author WBS
 * @date 2023/3/2 15:29
 * @desciption IReader
 */
public interface IReader {

    public void config();
    public DataTable readData();
    public DataTable readData(String sql);
}
