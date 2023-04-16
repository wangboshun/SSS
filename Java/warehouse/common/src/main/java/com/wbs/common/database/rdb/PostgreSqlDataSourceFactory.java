package com.wbs.common.database.rdb;

import cn.hutool.crypto.SecureUtil;
import com.wbs.common.database.base.model.DataSourceInfo;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

/**
 * @author WBS
 * @date 2023/4/15 11:49
 * @desciption PostgreSqlDataSourceFactory
 */
public class PostgreSqlDataSourceFactory implements DataSourceFactory {
    private DataSourceInfo sourceInfo;

    @Override
    public void config(DataSourceInfo info) {
        sourceInfo = info;
        String url = "jdbc:postgresql://" + sourceInfo.getHost() + ":" + sourceInfo.getPort() + "/" + sourceInfo.getDatabase() + "?currentSchema=" + sourceInfo.getSchema();
        sourceInfo.setUrl(url);
        sourceInfo.setName(SecureUtil.md5(url));
    }

    @Override
    public DataSource createDataSource() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(sourceInfo.getUrl());
        pgSimpleDataSource.setUser(sourceInfo.getUsername());
        pgSimpleDataSource.setPassword(sourceInfo.getPassword());
        return pgSimpleDataSource;
    }

    @Override
    public String getDataSourceName() {
        return sourceInfo.getName();
    }
}
