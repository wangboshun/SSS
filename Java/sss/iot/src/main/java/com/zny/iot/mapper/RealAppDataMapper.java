package com.zny.iot.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.iot.model.RealAppDataModel;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/26
 * 实时数据mapper
 */


@Repository
@DS("iot")
public interface RealAppDataMapper extends BaseMapper<RealAppDataModel> {

}