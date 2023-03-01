package com.wbs.iot.model.ctwing.dto;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption CtwingDeviceDataQueryInputDto
 */
public class CtwingDeviceDataQueryInputDto extends CtwingAuthDto {
    private String deviceId;
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
