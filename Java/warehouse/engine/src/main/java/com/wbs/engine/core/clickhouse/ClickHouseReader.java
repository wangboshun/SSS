package com.wbs.engine.core.clickhouse;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.engine.core.base.ReaderAbstract;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/3/2 15:36
 * @desciption ClickHouseReader
 */
@Component
public class ClickHouseReader extends ReaderAbstract {
    public ClickHouseReader() {
        this.dbType = DbTypeEnum.ClickHouse;
    }
}
