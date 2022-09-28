package com.zny.iot.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.iot.model.BXModel;
import com.zny.iot.model.StationBaseSetModel;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/26
 */


@Repository
@DS("iot")
public interface BXMapper extends BaseMapper<BXModel> {

}