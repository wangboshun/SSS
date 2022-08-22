package com.blue.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class User {
    public String name;

    @Value("blue2")
    public void setName(String name) {
        this.name = name;
    }
}
