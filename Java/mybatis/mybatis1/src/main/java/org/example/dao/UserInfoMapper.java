package org.example.dao;

import org.example.model.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/8/20
 */

public interface UserInfoMapper {
    List<UserInfo> getAllUserInfo();

    List<UserInfo> getUserInfoByLike(String name);

    List<UserInfo> getUserInfoByRowBounds();

    UserInfo getUserInfoById(String id);

    UserInfo getUserInfoByNameOrId(Map map);

    UserInfo getUserInfoByMap(Map<String, Object> map);


    int insertUserInfo(UserInfo userInfo);

    int updateUserInfo(UserInfo userInfo);

    int deleteUserInfo(String id);
}
