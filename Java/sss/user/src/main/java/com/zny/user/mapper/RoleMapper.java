package com.zny.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.user.model.role.RoleModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/2
 * 角色mapper
 */

@Repository
@Mapper
public interface RoleMapper extends BaseMapper<RoleModel> {
}
