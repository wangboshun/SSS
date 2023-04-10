package com.wbs.engine.core.sqlserver;

import com.wbs.common.database.base.DbTypeEnum;
import com.wbs.engine.core.base.WriterAbstract;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption SqlServerWriter
 */
@Component
public class SqlServerWriter extends WriterAbstract {
    public SqlServerWriter() {
        this.dbType = DbTypeEnum.SQLSERVER;
    }
}
