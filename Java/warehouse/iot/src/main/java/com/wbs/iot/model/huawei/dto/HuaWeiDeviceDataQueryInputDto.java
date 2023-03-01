package com.wbs.iot.model.huawei.dto;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption HuaWeiDeviceDataQueryInputDto
 */
public class HuaWeiDeviceDataQueryInputDto extends HuaWeiAuthDto {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
