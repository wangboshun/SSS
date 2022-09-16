package com.zny.user.model.role;

/**
 * @author WBS
 * Date:2022/9/14
 */

public class RoleTreeModel {
    private String id;
    private String role_name;
    private RoleTreeModel children;
    private int lelvel;

    public int getLelvel() {
        return lelvel;
    }

    public void setLelvel(int lelvel) {
        this.lelvel = lelvel;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleTreeModel getChildren() {
        return children;
    }

    public void setChildren(RoleTreeModel children) {
        this.children = children;
    }
}
