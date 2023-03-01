package com.wbs.iot.model.ctwing.dto;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption CtwingAuthDto
 */
public class CtwingAuthDto  implements Serializable {
    private String appKey;
    private String appSecret;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
