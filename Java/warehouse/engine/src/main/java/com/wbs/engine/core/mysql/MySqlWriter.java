package com.wbs.engine.core.mysql;

import com.wbs.common.database.DbTypeEnum;
import com.wbs.common.database.DbUtils;
import com.wbs.engine.core.base.WriterAbstract;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author WBS
 * @date 2023/3/2 15:47
 * @desciption MySqlWriter
 */
@Component
public class MySqlWriter extends WriterAbstract {
    public MySqlWriter() {
        this.dbType = DbTypeEnum.MySql;
    }
}
