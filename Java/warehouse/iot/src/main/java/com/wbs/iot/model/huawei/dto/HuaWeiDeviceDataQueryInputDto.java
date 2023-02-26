package com.wbs.iot.model.huawei.dto;

public class HuaWeiDeviceDataQueryInputDto extends HuaWeiAuthDto {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
