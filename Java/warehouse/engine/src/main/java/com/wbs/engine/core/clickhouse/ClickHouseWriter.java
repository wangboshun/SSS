package com.wbs.engine.core.clickhouse;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.engine.core.base.WriterAbstract;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption ClickHouseReaderWriter
 */
@Component
public class ClickHouseWriter extends WriterAbstract {
    public ClickHouseWriter() {
        this.dbType = DbTypeEnum.ClickHouse;
    }
}
