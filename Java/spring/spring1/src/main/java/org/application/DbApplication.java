package org.application;

import org.service.IDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author WBS
 * Date:2022/8/15
 */

public class DbApplication {

    @Autowired
    private IDataBase dataBase;

    @Value("abc")
    private String name;

    public DbApplication(){
        System.out.println("无参构造");
    }

    public DbApplication(int age){
        System.out.println("有参构造");
        System.out.println("age:"+age);
    }

    public void setDataBase(IDataBase dataBase) {
        this.dataBase = dataBase;
    }

    public void Test1(){
        System.out.println("name:"+name);
        this.dataBase.getDataBase();
    }

    public void setName(String name) {
        this.name = name;
    }
}
