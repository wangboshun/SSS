package org.example.dao;

import org.apache.ibatis.annotations.Select;
import org.example.model.UserInfo2;

import java.util.List;

/**
 * @author WBS
 * Date:2022/8/20
 */

public interface UserInfo3Mapper {

    @Select("select * from userinfo")
    List<UserInfo2> getAllUserInfo();
}
