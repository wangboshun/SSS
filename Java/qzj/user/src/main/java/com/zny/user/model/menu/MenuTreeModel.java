package com.zny.user.model.menu;

/**
 * @author WBS
 * Date:2022/9/14
 */

public class MenuTreeModel {
    public String id;
    public String menu_name;
    public MenuTreeModel children;
    public int lelvel;

    public int getLelvel() {
        return lelvel;
    }

    public void setLelvel(int lelvel) {
        this.lelvel = lelvel;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MenuTreeModel getChildren() {
        return children;
    }

    public void setChildren(MenuTreeModel children) {
        this.children = children;
    }
}
