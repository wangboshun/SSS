package com.wbs.engine.core.clickhouse;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.engine.core.base.WriterAbstract;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption ClickHouseReaderWriter
 */
public class ClickHouseWriter extends WriterAbstract {
    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList) {
        this.dbType = DbTypeEnum.CLICKHOUSE;
        super.config(tableName, connection, columnList);
    }
}
