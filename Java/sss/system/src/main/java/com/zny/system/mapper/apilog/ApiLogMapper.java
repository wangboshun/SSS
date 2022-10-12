package com.zny.system.mapper.apilog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.system.model.apilog.ApiLogModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Repository
@Mapper
public interface ApiLogMapper extends BaseMapper<ApiLogModel> {
}
