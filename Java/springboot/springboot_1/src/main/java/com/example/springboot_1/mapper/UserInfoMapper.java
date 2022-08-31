package com.example.springboot_1.mapper;

import com.example.springboot_1.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WBS
 * Date:2022/8/27
 */

@Mapper
@Repository
public interface UserInfoMapper {
    List<UserInfo> queryAll();

    UserInfo queryById(String id);

    int addUser(UserInfo user);

    int updateUser(UserInfo user);

    int deleteUser(String id);
}
