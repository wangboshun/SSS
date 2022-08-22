package org.example.model;

import java.util.Date;

/**
 * @author WBS
 * Date:2022/8/20
 */

public class UserInfo {
    private String Id;
    private String Name;
    private int Age;

    private Date TM;
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

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
}
