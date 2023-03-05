package com.wbs.engine.core.pgsql;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.engine.core.base.WriterAbstract;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption PgSqlWriter
 */
@Component
public class PgSqlWriter extends WriterAbstract {
    public PgSqlWriter() {
        this.dbType = DbTypeEnum.PostgreSql;
    }
}
