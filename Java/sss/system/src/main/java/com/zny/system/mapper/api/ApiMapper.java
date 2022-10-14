package com.zny.system.mapper.api;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.system.model.api.ApiModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/2
 * api mapper
 */

@Repository
@Mapper
public interface ApiMapper extends BaseMapper<ApiModel> {

}
