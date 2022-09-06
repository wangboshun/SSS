package com.zny.user.model;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author WBS
 * Date:2022/9/6
 */

@TableName("sys_role_map")
public class RoleMapModel {

    public String role_id;
    public String create_time;
    public Integer map_status;
    public String id;
    public String map_id;
    public String map_name;
    public int map_type;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getMap_status() {
        return map_status;
    }

    public void setMap_status(Integer map_status) {
        this.map_status = map_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public String getMap_name() {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public int getMap_type() {
        return map_type;
    }

    public void setMap_type(int map_type) {
        this.map_type = map_type;
    }
}
