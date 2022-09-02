package com.zny.system.model.apilog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Data
@TableName("sys_api_log")
public class ApiLogModel implements Serializable {

    public String id;
    public String user_id;
    public Float spend;
    public String url;
    public String method;
    public String params;
    public String ip;
    public Integer code;
    public String data;
    public String start_time;
    public String end_time;
}
