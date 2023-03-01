package com.wbs.iot.model.ali.dto;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption AliDeviceDataQueryInputDto
 */
public class AliDeviceDataQueryInputDto extends AliAuthDto {
    private String deviceName;
    private String productId;
    private String deviceId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
