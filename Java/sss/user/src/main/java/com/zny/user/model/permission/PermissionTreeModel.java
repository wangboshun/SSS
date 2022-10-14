package com.zny.user.model.permission;

/**
 * @author WBS
 * Date:2022/9/14
 * 权限树形类
 */

public class PermissionTreeModel {
    private String id;
    private String permission_name;
    private PermissionTreeModel children;
    private int lelvel;

    public int getLelvel() {
        return lelvel;
    }

    public void setLelvel(int lelvel) {
        this.lelvel = lelvel;
    }

    public String getPermission_name() {
        return permission_name;
    }

    public void setPermission_name(String permission_name) {
        this.permission_name = permission_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PermissionTreeModel getChildren() {
        return children;
    }

    public void setChildren(PermissionTreeModel children) {
        this.children = children;
    }
}
