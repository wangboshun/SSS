package com.zny.pipe.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.pipe.model.ConvertConfigModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date 2022-11-15 16:12
 * 转换配置mapper
 */

@Repository
@Mapper
public interface ConvertConfigMapper extends BaseMapper<ConvertConfigModel> {
}
