package com.example.springboot_1.pojo;

import lombok.Data;

import java.sql.Date;

/**
 * @author WBS
 * Date:2022/8/27
 */


@Data
public class UserInfo {
    public String Id;
    public String Name;
    public Date TM;
}
