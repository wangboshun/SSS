package com.wbs.common.database.rdb;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.db.GlobalDbConfig;
import com.clickhouse.jdbc.ClickHouseDataSource;
import com.wbs.common.database.base.model.DataSourceInfo;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/4/15 11:57
 * @desciption ClickHouseDataSourceFactory
 */
public class ClickHouseDataSourceFactory implements DataSourceFactory {
    private DataSourceInfo sourceInfo;

    public DataSourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(DataSourceInfo sourceInfo) {
        String url = "jdbc:clickhouse://" + sourceInfo.getHost() + ":" + sourceInfo.getPort() + "/" + sourceInfo.getDatabase();
        sourceInfo.setUrl(url);
        sourceInfo.setName(SecureUtil.md5(url));
        this.sourceInfo = sourceInfo;
    }

    @Override
    public DataSource createDataSource() {
        try {
            GlobalDbConfig.setReturnGeneratedKey(false);
            Properties properties = new Properties();
            properties.setProperty("user", sourceInfo.getUsername());
            properties.setProperty("password", sourceInfo.getPassword());
            return new ClickHouseDataSource(sourceInfo.getUrl(), properties);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getDataSourceName() {
        return sourceInfo.getName();
    }
}
