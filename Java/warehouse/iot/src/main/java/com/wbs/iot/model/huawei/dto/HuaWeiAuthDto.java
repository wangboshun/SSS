package com.wbs.iot.model.huawei.dto;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption HuaWeiAuthDto
 */
public class HuaWeiAuthDto implements Serializable {
    private String accessKeyId;
    private String secretAccessKey;
    private String region;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
