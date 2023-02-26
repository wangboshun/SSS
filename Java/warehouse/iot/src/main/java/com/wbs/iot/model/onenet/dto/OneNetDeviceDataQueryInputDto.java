package com.wbs.iot.model.onenet.dto;

public class OneNetDeviceDataQueryInputDto extends OneNetAuthDto {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
