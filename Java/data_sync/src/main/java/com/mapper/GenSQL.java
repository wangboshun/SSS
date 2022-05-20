package com.mapper;

import org.apache.ibatis.jdbc.SQL;

public class GenSQL {
    public String getSQL(final String id) {
        return new SQL() {
            //这个大括号 -> {} 表示构造代码块，表示每一次创建此对象，都会执行构造代码块中的代码
            {
                //表示查询学生表中的左右记录
                SELECT("*");
                FROM("Test1");
                if (id != null) {
                    WHERE("ID=#{id}");
                }
            }
            //toString()方法表示将构造的sql语句转化为一个字符串，并返回
        }.toString();
    }
}
