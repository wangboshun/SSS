package com.zny.user.model;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author WBS
 * Date:2022/9/6
 */
@TableName("sys_menu")
public class MenuModel {
    public String id;
    public String menu_name;
    public String menu_code;
    public String create_time;
    public Integer menu_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getMenu_status() {
        return menu_status;
    }

    public void setMenu_status(Integer menu_status) {
        this.menu_status = menu_status;
    }

    public String getMenu_code() {
        return menu_code;
    }

    public void setMenu_code(String menu_code) {
        this.menu_code = menu_code;
    }

}
