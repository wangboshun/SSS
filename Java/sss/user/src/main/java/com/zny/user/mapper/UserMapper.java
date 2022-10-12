package com.zny.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.user.model.user.UserModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Repository
@Mapper
public interface UserMapper extends BaseMapper<UserModel> {
}
