package com.zny.user.model.menu;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author WBS
 * Date:2022/9/6
 */
@TableName("sys_menu")
public class MenuModel {
    private String id;
    private String menu_name;
    private String menu_code;
    private String parent_id;
    private String create_time;
    private Integer menu_status;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

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
