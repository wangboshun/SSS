package com.zny.pipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.pipe.model.SinkConfigModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SinkConfigMapper extends BaseMapper<SinkConfigModel> {
}
