package com.zny.pipe.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.pipe.model.ConnectConfigModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date 2022-11-15 16:12
 * 连接配置mapper
 */

@Repository
@Mapper
public interface ConnectConfigMapper extends BaseMapper<ConnectConfigModel> {
}
