package com.zny.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.user.model.PermissionModel;
import com.zny.user.model.RoleModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Repository
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionModel> {
}