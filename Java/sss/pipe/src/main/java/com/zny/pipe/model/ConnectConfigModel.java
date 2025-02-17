package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date 2022-11-15 15:37
 * 链接配置类
 */

@TableName("pipe_connect_config")
public class ConnectConfigModel implements Serializable {

    @TableId
    private String id;
    private String connect_name;

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库
     */
    private String db_name;


    /**
     * 数据库模式，pgsql会用到
     */
    private String db_schema;

    public String getDb_schema() {
        return db_schema;
    }

    public void setDb_schema(String db_schema) {
        this.db_schema = db_schema;
    }

    /**
     * 数据库类型
     * 0为mysql、1为mssql
     */
    private Integer db_type;

    private String create_time;
    private Integer connect_status;

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnect_name() {
        return connect_name;
    }

    public void setConnect_name(String connect_name) {
        this.connect_name = connect_name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDb_type() {
        return db_type;
    }

    public void setDb_type(Integer db_type) {
        this.db_type = db_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getConnect_status() {
        return connect_status;
    }

    public void setConnect_status(Integer connect_status) {
        this.connect_status = connect_status;
    }
}
