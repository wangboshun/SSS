package com.zny.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Data
@TableName("sys_user")
public class UserModel {
    public String id;
    public String user_name;
    public String password;
    public LocalDateTime create_time;
    public Integer user_status;
}
