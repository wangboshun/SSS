package com.zny.pipe.appication;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
import com.zny.common.utils.DateUtils;
import com.zny.pipe.mapper.ConvertConfigMapper;
import com.zny.pipe.model.ConvertConfigModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConvertConfigApplication extends ServiceImpl<ConvertConfigMapper, ConvertConfigModel> {

    /**
     * 根据id获取转换条件
     *
     * @param taskId 任务id
     */
    public List<ConvertConfigModel> getConvertByTaskId(String taskId) {
        QueryWrapper<ConvertConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId);
        List<ConvertConfigModel> list = this.list(wrapper);
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    /**
     * 根据id获取转换条件
     *
     * @param id id
     */
    public SaResult getConvertById(String id) {
        ConvertConfigModel model = this.getById(id);
        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "转换条件不存在！");
        }
        return SaResult.data(model);
    }

    /**
     * 添加转换条件
     */
    public SaResult addConvert(String taskId, String convertField, String filterSymbol, String convertAfter,String convertSymbol, String convertBefore) {
        QueryWrapper<ConvertConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId);
        wrapper.eq("convert_field", convertField);
        wrapper.eq("filter_symbol", filterSymbol);
        wrapper.eq("convert_after", convertAfter);
        wrapper.eq("convert_symbol", convertSymbol);
        ConvertConfigModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("转换条件已存在！");
        }
        model = new ConvertConfigModel();
        model.setId(UUID.randomUUID().toString());
        model.setTask_id(taskId);
        model.setConvert_field(convertField);
        model.setFilter_symbol(filterSymbol);
        model.setConvert_after(convertAfter);
        model.setConvert_symbol(convertSymbol);
        model.setConvert_before(convertBefore);
        model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        if (save(model)) {
            return SaResult.ok("添加转换条件成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加转换条件失败！");
        }
    }

    /**
     * 删除转换条件
     *
     * @param id 用户id
     */
    public SaResult deleteConvert(String id) {
        QueryWrapper<ConvertConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        ConvertConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "转换条件不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除转换条件成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除转换条件失败！");
        }
    }

    /**
     * 更新转换条件信息
     */
    public SaResult updateConvert(String id, String convertField, String filterSymbol, String convertAfter, String convertSymbol, String convertBefore) {
        QueryWrapper<ConvertConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        ConvertConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "转换条件不存在！");
        }
        if (StringUtils.isNotBlank(convertField)) {
            model.setConvert_field(convertField);
        }
        if (StringUtils.isNotBlank(filterSymbol)) {
            model.setFilter_symbol(filterSymbol);
        }
        if (StringUtils.isNotBlank(convertAfter)) {
            model.setConvert_after(convertAfter);
        }
        if (StringUtils.isNotBlank(convertSymbol)) {
            model.setConvert_symbol(convertSymbol);
        }
        if (StringUtils.isNotBlank(convertBefore)) {
            model.setConvert_before(convertBefore);
        }
        if (updateById(model)) {
            return SaResult.ok("更新转换条件信息成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除转换条件信息失败！");
        }
    }

}
