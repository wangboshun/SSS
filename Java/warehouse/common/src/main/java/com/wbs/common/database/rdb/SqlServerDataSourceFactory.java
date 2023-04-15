package com.wbs.common.database.rdb;

import cn.hutool.crypto.SecureUtil;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.wbs.common.database.base.model.DataSourceInfo;

import javax.sql.DataSource;

/**
 * @author WBS
 * @date 2023/4/15 11:44
 * @desciption SqlServerDataSourceFactory
 */
public class SqlServerDataSourceFactory implements DataSourceFactory {
    private DataSourceInfo sourceInfo;

    public DataSourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(DataSourceInfo sourceInfo) {
        String url = "jdbc:sqlserver://" + sourceInfo.getHost() + ":" + sourceInfo.getPort() + ";database=" + sourceInfo.getDatabase() + ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
        sourceInfo.setUrl(url);
        sourceInfo.setName(SecureUtil.md5(url));
        this.sourceInfo = sourceInfo;
    }

    @Override
    public DataSource createDataSource() {
        SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
        sqlServerDataSource.setURL(sourceInfo.getUrl());
        sqlServerDataSource.setUser(sourceInfo.getUsername());
        sqlServerDataSource.setPassword(sourceInfo.getPassword());
        return sqlServerDataSource;
    }

    @Override
    public String getDataSourceName() {
        return sourceInfo.getName();
    }
}
