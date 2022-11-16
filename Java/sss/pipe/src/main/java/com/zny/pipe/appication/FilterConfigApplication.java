package com.zny.pipe.appication;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
import com.zny.common.utils.DateUtils;
import com.zny.pipe.mapper.FilterConfigMapper;
import com.zny.pipe.model.FilterConfigModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FilterConfigApplication extends ServiceImpl<FilterConfigMapper, FilterConfigModel> {

    /**
     * 根据id获取过滤条件
     *
     * @param taskId 任务id
     */
    public List<FilterConfigModel> getFilterByTaskId(String taskId) {
        QueryWrapper<FilterConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId);
        List<FilterConfigModel> list = this.list(wrapper);
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    /**
     * 根据id获取过滤条件
     *
     * @param id id
     */
    public SaResult getFilterById(String id) {
        FilterConfigModel model = this.getById(id);
        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "过滤条件不存在！");
        }
        return SaResult.data(model);
    }

    /**
     * 添加过滤条件
     */
    public SaResult addFilter(String taskId, String filterColumn, String filterSymbol, String filterValue, String filterType, Integer useType) {
        QueryWrapper<FilterConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId);
        wrapper.eq("filter_column", filterColumn);
        wrapper.eq("filter_symbol", filterSymbol);
        wrapper.eq("use_type", useType);
        FilterConfigModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("过滤条件已存在！");
        }
        model = new FilterConfigModel();
        model.setId(UUID.randomUUID().toString());
        model.setFilter_column(filterColumn);
        model.setTask_id(taskId);
        model.setFilter_symbol(filterSymbol);
        model.setFilter_value(filterValue);
        model.setFilter_type(filterType);
        model.setUse_type(useType);
        model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        if (save(model)) {
            return SaResult.ok("添加过滤条件成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加过滤条件失败！");
        }
    }

    /**
     * 删除过滤条件
     *
     * @param id 用户id
     */
    public SaResult deleteFilter(String id) {
        QueryWrapper<FilterConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        FilterConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "过滤条件不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除过滤条件成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除过滤条件失败！");
        }
    }

    /**
     * 更新过滤条件信息
     */
    public SaResult updateFilter(String id, String filterColumn, String filterSymbol, String filterValue, String filterType, Integer useType) {
        QueryWrapper<FilterConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        FilterConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "过滤条件不存在！");
        }
        if (StringUtils.isNotBlank(filterColumn)) {
            model.setFilter_column(filterColumn);
        }
        if (StringUtils.isNotBlank(filterSymbol)) {
            model.setFilter_symbol(filterSymbol);
        }
        if (StringUtils.isNotBlank(filterValue)) {
            model.setFilter_value(filterValue);
        }
        if (StringUtils.isNotBlank(filterType)) {
            model.setFilter_type(filterType);
        }
        if (useType != null) {
            model.setUse_type(useType);
        }
        if (updateById(model)) {
            return SaResult.ok("更新过滤条件信息成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除过滤条件信息失败！");
        }
    }

}
