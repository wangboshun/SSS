package com.mapper;

import org.apache.ibatis.jdbc.SQL;

public class GenSQL {
    public String getSQL(final String id) {
        SQL s = new SQL();
        s.SELECT("*");
        s.FROM("Test1");
        if (id != null) {
            s.WHERE("ID=#{id}");
        }
        return s.toString();
    }
}
