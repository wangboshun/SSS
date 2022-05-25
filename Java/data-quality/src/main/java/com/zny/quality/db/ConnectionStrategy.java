package com.zny.quality.db;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author WBS
 */
public interface ConnectionStrategy {

    /**
     * 获取连接
     * @param url 连接字符串
     * @return 连接
     */
    Connection getConnection(String url);


    /**
     * 获取连接
     * @param url 连接字符串
     * @param name 用户
     * @param password  密码
     * @return 连接
     */
    Connection getConnection(String url,String name,String password);


    /**
     * 获取连接
     * @param host 主机
     * @param port 端口
     * @param db 数据库
     * @param name 用户
     * @param password 密码
     * @return 连接
     */
    Connection getConnection(String host,int port,String db,String name,String password);
}
