package com.zny.user.model.user;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/14
 */

public class UserTreeModel implements Serializable {
    private String id;
    private String user_name;
    private UserTreeModel children;
    private int lelvel;

    public int getLelvel() {
        return lelvel;
    }

    public void setLelvel(int lelvel) {
        this.lelvel = lelvel;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserTreeModel getChildren() {
        return children;
    }

    public void setChildren(UserTreeModel children) {
        this.children = children;
    }
}
