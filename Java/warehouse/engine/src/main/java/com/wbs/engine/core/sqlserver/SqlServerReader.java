package com.wbs.engine.core.sqlserver;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.engine.core.base.ReaderAbstract;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/3/2 15:36
 * @desciption SqlServerReader
 */
@Component
public class SqlServerReader extends ReaderAbstract {
    public SqlServerReader() {
        this.dbType = DbTypeEnum.SqlServer;
    }
}
