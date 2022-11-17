package com.zny.pipe.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zny.pipe.model.TaskConfigModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WBS
 * Date 2022-11-15 16:12
 * 任务配置mapper
 */

@Repository
@Mapper
public interface TaskConfigMapper extends BaseMapper<TaskConfigModel> {
}
