package com.mapper;

import com.domain.Test1;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface Test1Mapper {
    int deleteByPrimaryKey(@Param("id") String id, @Param("tm") Date tm);

    int insert(Test1 record);

    int insertSelective(Test1 record);

    Test1 selectByPrimaryKey(@Param("id") String id);

    int updateByPrimaryKeySelective(Test1 record);

    int updateByPrimaryKey(Test1 record);
}