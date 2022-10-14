package com.zny.common.resource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date:2022/9/2
 * 资源mapper
 */

@Repository
@Mapper
public interface ResourceMapper extends BaseMapper<ResourceModel> {
}
