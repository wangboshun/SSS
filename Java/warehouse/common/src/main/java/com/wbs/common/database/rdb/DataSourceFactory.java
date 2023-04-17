package com.wbs.common.database.rdb;

import com.wbs.common.database.base.model.DataSourceInfo;

import javax.sql.DataSource;

/**
 * @author WBS
 * @date 2023/4/15 11:43
 * @desciption DataSourceFactory
 */
public interface DataSourceFactory {

    void config(DataSourceInfo info);

    DataSource createDataSource();

    String getDataSourceName();
}
