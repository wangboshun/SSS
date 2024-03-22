package com.wbs.common.extend;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/4/6 15:51
 * @desciption BaseModel
 */
@Setter
@Getter
public class BaseModel implements Serializable {
    private String id;
    private LocalDateTime ct;
    private LocalDateTime ut;
}
