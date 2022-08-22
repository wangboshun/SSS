package org.example.model;

import java.util.Date;

/**
 * @author WBS
 * Date:2022/8/20
 */

public class UserInfo2 {
    private String UserId;
    private String UserName;
    private int Age;

    private Date TM;

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public Date getTM() {
        return TM;
    }

    public void setTM(Date TM) {
        this.TM = TM;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
