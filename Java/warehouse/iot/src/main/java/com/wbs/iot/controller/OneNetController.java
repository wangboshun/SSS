package com.wbs.iot.controller;

import com.wbs.iot.application.OneNetApplication;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.onenet.dto.OneNetAuthDto;
import com.wbs.iot.model.onenet.dto.OneNetDeviceDataQueryInputDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption OneNetController
 */
@RestController
@RequestMapping("/iot/onenet")
@Tag(name = "iot", description = "iot模块")
public class OneNetController {
    private OneNetApplication oneNetApplication;

    public OneNetController(OneNetApplication oneNetApplication) {
        this.oneNetApplication = oneNetApplication;
    }

    /**
     * 获取所有设备
     *
     * @param input
     * @return
     */
    @GetMapping("/get_deivce_list")
    public List<DeviceInfoModel> getDeviceList(OneNetAuthDto input) {
        Properties properties = new Properties();
        properties.setProperty("masterKey", input.getMasterKey());
        oneNetApplication.config(properties);
        List<DeviceInfoModel> list = oneNetApplication.getDeviceList(null);
        return list;
    }

    /**
     * 获取指定设备当前数据
     *
     * @param input
     * @return
     */
    @GetMapping("/get_data_list")
    public List<DeviceDataModel> getDeviceData(OneNetDeviceDataQueryInputDto input) {
        Properties properties = new Properties();
        properties.setProperty("masterKey", input.getMasterKey());
        oneNetApplication.config(properties);
        DeviceInfoModel device = new DeviceInfoModel();
        device.setId(input.getDeviceId());
        List<DeviceDataModel> list = oneNetApplication.getDeviceData(device);
        return list;
    }
}
