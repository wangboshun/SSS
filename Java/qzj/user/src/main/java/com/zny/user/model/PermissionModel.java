package com.zny.user.model;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author WBS
 * Date:2022/9/6
 */
@TableName("sys_permission")
public class PermissionModel {
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermission_name() {
        return permission_name;
    }

    public void setPermission_name(String permission_name) {
        this.permission_name = permission_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getPermission_status() {
        return permission_status;
    }

    public void setPermission_status(Integer permission_status) {
        this.permission_status = permission_status;
    }

    public String getPermission_code() {
        return permission_code;
    }

    public void setPermission_code(String permission_code) {
        this.permission_code = permission_code;
    }

    public String permission_name;
    public String create_time;
    public Integer permission_status;
    private String permission_code;

}
