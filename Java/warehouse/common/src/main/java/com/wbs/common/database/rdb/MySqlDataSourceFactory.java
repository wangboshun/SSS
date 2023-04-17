package com.wbs.common.database.rdb;

import cn.hutool.crypto.SecureUtil;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.wbs.common.database.base.model.DataSourceInfo;

import javax.sql.DataSource;

/**
 * @author WBS
 * @date 2023/4/15 11:44
 * @desciption MysqlDataSourceFactory
 */
public class MySqlDataSourceFactory implements DataSourceFactory {
    private DataSourceInfo sourceInfo;

    @Override
    public void config(DataSourceInfo info) {
        sourceInfo = info;
        String url = "jdbc:mysql://" + info.getHost() + ":" + info.getPort() + "/" + info.getDatabase() + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&rewriteBatchedStatements=true&useInformationSchema=true&characterEncoding=UTF-8";
        sourceInfo.setUrl(url);
        sourceInfo.setName(SecureUtil.md5(url));
    }

    @Override
    public DataSource createDataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(sourceInfo.getUrl());
        mysqlDataSource.setUser(sourceInfo.getUsername());
        mysqlDataSource.setPassword(sourceInfo.getPassword());
        return mysqlDataSource;
    }

    @Override
    public String getDataSourceName() {
        return sourceInfo.getName();
    }
}
