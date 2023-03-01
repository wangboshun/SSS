package com.wbs.iot.model.onenet.dto;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption OneNetDeviceDataQueryInputDto
 */
public class OneNetDeviceDataQueryInputDto extends OneNetAuthDto {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
