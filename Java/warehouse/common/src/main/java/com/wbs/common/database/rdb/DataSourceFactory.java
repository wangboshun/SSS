package com.wbs.common.database.rdb;

import javax.sql.DataSource;

/**
 * @author WBS
 * @date 2023/4/15 11:43
 * @desciption DataSourceFactory
 */
public interface DataSourceFactory {
    DataSource createDataSource();

    String getDataSourceName();
}
