package com.wbs.engine.core.clickhouse;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.common.database.base.model.ColumnInfo;
import com.wbs.common.database.base.model.WhereInfo;
import com.wbs.engine.core.base.ReaderAbstract;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.List;

/**
 * @author WBS
 * @date 2023/3/2 15:36
 * @desciption ClickHouseReader
 */
public class ClickHouseReader extends ReaderAbstract {
    @Override
    public void config(String tableName, Connection connection, List<ColumnInfo> columnList, List<WhereInfo> whereList) {
        this.dbType = DbTypeEnum.CLICKHOUSE;
        super.config(tableName, connection, columnList,whereList);
    }
}
