package com.zny.pipe.appication;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
import com.zny.common.utils.DateUtils;
import com.zny.pipe.mapper.ColumnConfigMapper;
import com.zny.pipe.model.ColumnConfigModel;
import com.zny.pipe.model.dto.ColumnConfigDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author WBS
 * Date:2022/11/17
 * 字段映射配置服务类
 */

@Service
public class ColumnConfigApplication extends ServiceImpl<ColumnConfigMapper, ColumnConfigModel> {

    /**
     * 根据id获取字段配置
     *
     * @param taskId 任务id
     */
    public List<ColumnConfigModel> getColumnByTaskId(String taskId) {
        QueryWrapper<ColumnConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId);
        List<ColumnConfigModel> list = this.list(wrapper);
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    /**
     * 根据id获取字段配置
     *
     * @param id id
     */
    public SaResult getColumnById(String id) {
        ColumnConfigModel model = this.getById(id);
        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "字段配置不存在！");
        }
        return SaResult.data(model);
    }

    /**
     * 添加字段配置
     *
     * @param cloumnData 配置集合
     */
    public SaResult addColumn(List<ColumnConfigDto> cloumnData) {
        String taskId = cloumnData.get(0).getTask_id();
        QueryWrapper<ColumnConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId);
        ColumnConfigModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("字段配置已存在！");
        }
        List<ColumnConfigModel> list = new ArrayList<>();
        for (ColumnConfigDto item : cloumnData) {
            model = new ColumnConfigModel();
            model.setId(UUID.randomUUID().toString());
            model.setTask_id(taskId);
            model.setSink_column(item.getSink_column());
            model.setSource_column(item.getSource_column());
            model.setDefault_value(item.getDefault_value());
            model.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
            list.add(model);
        }

        if (saveBatch(list)) {
            return SaResult.ok("添加字段配置成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "添加字段配置失败！");
        }
    }

    /**
     * 删除字段配置
     *
     * @param id id
     */
    public SaResult deleteColumn(String id) {
        QueryWrapper<ColumnConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        ColumnConfigModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResultEx.error(MessageCodeEnum.NOT_FOUND, "字段配置不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除字段配置成功！");
        } else {
            return SaResultEx.error(MessageCodeEnum.DB_ERROR, "删除字段配置失败！");
        }
    }

    /**
     * 根据任务id删除字段配置
     *
     * @param taskId 任务id
     */
    public SaResult deleteColumnByTask(String taskId) {
        QueryWrapper<ColumnConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId);
        List<ColumnConfigModel> list = this.list(wrapper);
        if (list.isEmpty()) {
            return SaResult.error("字段配置不存在！");
        } else {
            if (this.removeBatchByIds(list.stream().map(ColumnConfigModel::getId).collect(Collectors.toList()))) {
                return SaResult.ok("删除字段配置成功！");
            } else {
                return SaResultEx.error(MessageCodeEnum.DB_ERROR, "修改字段配置失败！");
            }
        }
    }

    /**
     * 更新字段配置信息
     *
     * @param cloumnData 配置集合
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public SaResult updateColumn(List<ColumnConfigDto> cloumnData) {
        String taskId = cloumnData.get(0).getTask_id();
        QueryWrapper<ColumnConfigModel> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId);
        List<ColumnConfigModel> list = this.list(wrapper);
        if (list.isEmpty()) {
            return SaResult.error("字段配置不存在！");
        } else {
            if (this.removeBatchByIds(list.stream().map(ColumnConfigModel::getId).collect(Collectors.toList()))) {
                return addColumn(cloumnData);
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return SaResultEx.error(MessageCodeEnum.DB_ERROR, "修改字段配置失败！");
            }
        }
    }
}
