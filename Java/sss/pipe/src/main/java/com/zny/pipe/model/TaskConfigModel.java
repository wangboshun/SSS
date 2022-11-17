package com.zny.pipe.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date 2022-11-15 15:37
 * 任务配置类
 */

@TableName("pipe_task_config")
public class TaskConfigModel implements Serializable {

    @TableId
    private String id;
    private String task_name;

    /**
     * 目的节点
     */
    private String sink_id;

    /**
     * 目的节点类型
     */
    private Integer sink_type;

    /**
     * 源节点
     */
    private String source_id;

    /**
     * 源节点类型
     */
    private Integer source_type;
    /**
     * 开始时间
     */
    private String start_time;

    /**
     * 结束时间
     */
    private String end_time;

    /**
     * 步长
     */
    private Integer time_step;

    /**
     * 插入方式
     * 0为存在跳过、1为存在更新
     */
    private Integer insert_type;
    private String create_time;
    private Integer task_status;

    /**
     * where查询条件
     * 存储写好的sql where条件
     */
    private String where_param;

    /**
     * 任务执行类型
     * 0为间隔时间、1为cron
     */
    private Integer execute_type;

    /**
     * 执行参数
     * 0为间隔时间，单位秒、1为cron表达式
     */
    private String execute_param;

    /**
     * 新增方式
     * 0为增量、1为全量
     */
    private Integer add_type;

    public Integer getSink_type() {
        return sink_type;
    }

    public void setSink_type(Integer sink_type) {
        this.sink_type = sink_type;
    }

    public Integer getSource_type() {
        return source_type;
    }

    public void setSource_type(Integer source_type) {
        this.source_type = source_type;
    }

    public Integer getAdd_type() {
        return add_type;
    }

    public void setAdd_type(Integer add_type) {
        this.add_type = add_type;
    }

    public Integer getExecute_type() {
        return execute_type;
    }

    public void setExecute_type(Integer execute_type) {
        this.execute_type = execute_type;
    }

    public String getExecute_param() {
        return execute_param;
    }

    public void setExecute_param(String execute_param) {
        this.execute_param = execute_param;
    }

    public String getWhere_param() {
        return where_param;
    }

    public void setWhere_param(String where_param) {
        this.where_param = where_param;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getSink_id() {
        return sink_id;
    }

    public void setSink_id(String sink_id) {
        this.sink_id = sink_id;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Integer getTime_step() {
        return time_step;
    }

    public void setTime_step(Integer time_step) {
        this.time_step = time_step;
    }

    public Integer getInsert_type() {
        return insert_type;
    }

    public void setInsert_type(Integer insert_type) {
        this.insert_type = insert_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getTask_status() {
        return task_status;
    }

    public void setTask_status(Integer task_status) {
        this.task_status = task_status;
    }
}
