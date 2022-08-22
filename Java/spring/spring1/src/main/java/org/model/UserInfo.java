package org.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/8/17
 */

@Component
public class UserInfo {

    @Value("wbs")
    private String name;

    public String getName(){
        return name;
    }
}
