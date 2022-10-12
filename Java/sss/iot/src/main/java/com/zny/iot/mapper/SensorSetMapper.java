package com.zny.iot.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.iot.model.SensorSetModel;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/26
 */


@Repository
@DS("iot")
public interface SensorSetMapper extends BaseMapper<SensorSetModel> {
    @Select("select top 1 * from SensorSet order by SensorID desc ")
    Integer getMaxId();
}