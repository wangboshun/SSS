package com.wbs.iot.controller;

import com.wbs.iot.application.HuaWeiApplication;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import com.wbs.iot.model.huawei.dto.HuaWeiAuthDto;
import com.wbs.iot.model.huawei.dto.HuaWeiDeviceDataQueryInputDto;
import com.wbs.iot.model.huawei.dto.HuaWeiDeviceQueryInputDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption HuaweiController
 */
@RestController
@RequestMapping("/iot/huawei")
@Tag(name = "iot", description = "iot模块")
public class HuaWeiController {
    private HuaWeiApplication huaWeiApplication;

    public HuaWeiController(HuaWeiApplication huaWeiApplication) {
        this.huaWeiApplication = huaWeiApplication;
    }

    /**
     * 获取所有产品
     *
     * @param input
     * @return
     */
    @GetMapping("/get_product_list")
    public List<ProductInfoModel> getProductList(HuaWeiAuthDto input) {
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", input.getAccessKeyId());
        properties.setProperty("secretAccessKey", input.getSecretAccessKey());
        properties.setProperty("region", input.getRegion());
        huaWeiApplication.config(properties);
        List<ProductInfoModel> list = huaWeiApplication.getProductList();
        return list;
    }

    /**
     * 获取所有设备
     *
     * @param input
     * @return
     */
    @GetMapping("/get_deivce_list")
    public List<DeviceInfoModel> getDeviceList(HuaWeiAuthDto input) {
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", input.getAccessKeyId());
        properties.setProperty("secretAccessKey", input.getSecretAccessKey());
        properties.setProperty("region", input.getRegion());
        huaWeiApplication.config(properties);
        List<ProductInfoModel> productList = huaWeiApplication.getProductList();
        List<DeviceInfoModel> list = new ArrayList<>();
        for (ProductInfoModel product : productList) {
            List<DeviceInfoModel> deviceList = huaWeiApplication.getDeviceList(product);
            list.addAll(deviceList);
        }
        return list;
    }

    /**
     * 获取指定产品下所有设备
     *
     * @param input
     * @return
     */
    @GetMapping("/get_deivce_by_product")
    public List<DeviceInfoModel> getDeviceListByProduct(HuaWeiDeviceQueryInputDto input) {
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", input.getAccessKeyId());
        properties.setProperty("secretAccessKey", input.getSecretAccessKey());
        properties.setProperty("region", input.getRegion());
        huaWeiApplication.config(properties);
        ProductInfoModel product = new ProductInfoModel();
        product.setId(input.getProductId());
        List<DeviceInfoModel> list = huaWeiApplication.getDeviceList(product);
        return list;
    }

    /**
     * 获取指定设备当前数据
     *
     * @param input
     * @return
     */
    @GetMapping("/get_data_list")
    public List<DeviceDataModel> getDeviceData(HuaWeiDeviceDataQueryInputDto input) {
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", input.getAccessKeyId());
        properties.setProperty("secretAccessKey", input.getSecretAccessKey());
        properties.setProperty("region", input.getRegion());
        huaWeiApplication.config(properties);
        DeviceInfoModel device = new DeviceInfoModel();
        device.setId(input.getDeviceId());
        List<DeviceDataModel> list = huaWeiApplication.getDeviceData(device);
        return list;
    }
}
