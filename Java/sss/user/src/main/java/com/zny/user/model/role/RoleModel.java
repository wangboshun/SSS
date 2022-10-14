package com.zny.user.model.role;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/2
 * 角色类
 */

@TableName("sys_role")
public class RoleModel implements Serializable {
    private String id;
    private String role_name;
    private String create_time;
    private Integer role_status;
    private String parent_id;
    private String role_code;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "RoleModel{" + "id='" + id + '\'' + ", role_name='" + role_name + '\'' + ", create_time='" + create_time + '\'' + ", role_status=" + role_status + ", parent_id='" + parent_id + '\'' + ", role_code='" + role_code + '\'' + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getRole_status() {
        return role_status;
    }

    public void setRole_status(Integer role_status) {
        this.role_status = role_status;
    }

    public String getRole_code() {
        return role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }
}
