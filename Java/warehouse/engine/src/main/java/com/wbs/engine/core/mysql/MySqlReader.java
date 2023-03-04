package com.wbs.engine.core.mysql;

import com.wbs.common.database.DbTypeEnum;
import com.wbs.engine.core.base.ReaderAbstract;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/3/2 15:36
 * @desciption MySqlReader
 */
@Component
public class MySqlReader extends ReaderAbstract {
    public MySqlReader() {
        this.dbType = DbTypeEnum.MySql;
    }
}
