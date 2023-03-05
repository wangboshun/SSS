package com.wbs.iot.model.ali.dto;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption AliAuthDto
 */
public class AliAuthDto implements Serializable {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
