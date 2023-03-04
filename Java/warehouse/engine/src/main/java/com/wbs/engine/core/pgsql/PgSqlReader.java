package com.wbs.engine.core.pgsql;

import com.wbs.common.database.DbTypeEnum;
import com.wbs.engine.core.base.ReaderAbstract;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/3/2 15:36
 * @desciption PgSqlReader
 */
@Component
public class PgSqlReader extends ReaderAbstract {
    public PgSqlReader() {
        this.dbType = DbTypeEnum.PostgreSql;
    }
}
