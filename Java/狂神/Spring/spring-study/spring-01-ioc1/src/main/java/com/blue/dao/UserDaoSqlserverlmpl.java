package com.blue.dao;

public class UserDaoSqlserverlmpl implements UserDao{
    @Override
    public void getUser() {
        System.out.println("Sqlserver获取用户数据！");
    }
}
