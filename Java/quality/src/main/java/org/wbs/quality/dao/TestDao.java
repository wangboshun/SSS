package org.wbs.quality.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wbs.quality.model.Test1;

/**
 * @author WBS
 * Date:2022/6/1
 */


@Mapper
public interface TestDao {

    @Select("select Id,TM,SJ from test1 where id= #{id} ")
    public Test1 getById(String id);
}
