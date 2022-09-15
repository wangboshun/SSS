package com.zny.user.model.user;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/2
 */

@TableName("sys_user")
public class UserModel implements Serializable {
    public String id;
    public String user_name;
    public String password;
    public String parent_id;
    /**
     * @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     * 时间格式化
     */
    public String create_time;
    public Integer user_status;
    public Integer user_type;

    public Integer getUser_type() {
        return user_type;
    }

    public void setUser_type(Integer user_type) {
        this.user_type = user_type;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "UserModel{" + "id='" + id + '\'' + ", user_name='" + user_name + '\'' + ", password='" + password + '\'' + ", create_time='" + create_time + '\'' + ", user_status=" + user_status + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getUser_status() {
        return user_status;
    }

    public void setUser_status(Integer user_status) {
        this.user_status = user_status;
    }
}
