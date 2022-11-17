package com.zny.pipe.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.pipe.model.FilterConfigModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date 2022-11-15 16:12
 * 过滤配置mapper
 */

@Repository
@Mapper
public interface FilterConfigMapper extends BaseMapper<FilterConfigModel> {
}
