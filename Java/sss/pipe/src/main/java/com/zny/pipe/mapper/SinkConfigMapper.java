package com.zny.pipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.pipe.model.SinkConfigModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date 2022-11-15 16:12
 * 目的配置mapper
 */

@Repository
@Mapper
public interface SinkConfigMapper extends BaseMapper<SinkConfigModel> {
}
