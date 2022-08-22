package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author WBS
 * Date:2022/8/20
 */

@Data

//有参构造
@AllArgsConstructor

//无参构造
@NoArgsConstructor
public class UserInfo3 {
    private String UserId;
    private String UserName;
    private int Age;
    private Date TM;
}
