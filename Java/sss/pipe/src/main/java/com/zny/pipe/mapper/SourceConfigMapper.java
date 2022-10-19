package com.zny.pipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.pipe.model.SourceConfigModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SourceConfigMapper extends BaseMapper<SourceConfigModel> {
}
