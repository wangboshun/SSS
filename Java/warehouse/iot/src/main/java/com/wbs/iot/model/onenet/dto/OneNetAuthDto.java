package com.wbs.iot.model.onenet.dto;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption OneNetAuthDto
 */
public class OneNetAuthDto  implements Serializable {
    private String masterKey;

    public String getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }
}
