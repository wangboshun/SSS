package com.wbs.iot.controller;

import com.wbs.iot.application.AliApplication;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import com.wbs.iot.model.ali.dto.AliAuthDto;
import com.wbs.iot.model.ali.dto.AliDeviceDataQueryInputDto;
import com.wbs.iot.model.ali.dto.AliDeviceQueryInputDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/2/23 9:57
 * @desciption AliController
 */

@RestController
@RequestMapping("/iot/ali")
@Tag(name = "iot", description = "iot模块")
public class AliController {
    private AliApplication aliApplication;

    public AliController(AliApplication aliApplication) {
        this.aliApplication = aliApplication;
    }

    /**
     * 获取所有产品
     *
     * @param input
     * @return
     */
    @GetMapping("/get_product_list")
    public List<ProductInfoModel> getProductList(AliAuthDto input) {
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", input.getAccessKeyId());
        properties.setProperty("accessKeySecret", input.getAccessKeySecret());
        properties.setProperty("endpoint", input.getEndpoint());
        aliApplication.config(properties);
        List<ProductInfoModel> list = aliApplication.getProductList();
        return list;
    }

    /**
     * 获取所有设备
     *
     * @param input
     * @return
     */
    @GetMapping("/get_deivce_list")
    public List<DeviceInfoModel> getDeviceList(AliAuthDto input) {
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", input.getAccessKeyId());
        properties.setProperty("accessKeySecret", input.getAccessKeySecret());
        properties.setProperty("endpoint", input.getEndpoint());
        aliApplication.config(properties);
        List<ProductInfoModel> productList = aliApplication.getProductList();

        List<DeviceInfoModel> list = new ArrayList<>();
        for (ProductInfoModel product : productList) {
            List<DeviceInfoModel> deviceList = aliApplication.getDeviceList(product);
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
    public List<DeviceInfoModel> getDeviceListByProduct(AliDeviceQueryInputDto input) {
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", input.getAccessKeyId());
        properties.setProperty("accessKeySecret", input.getAccessKeySecret());
        properties.setProperty("endpoint", input.getEndpoint());
        aliApplication.config(properties);
        ProductInfoModel product = new ProductInfoModel();
        product.setId(input.getProductId());
        List<DeviceInfoModel> list = aliApplication.getDeviceList(product);
        return list;
    }

    /**
     * 获取指定设备当前数据
     *
     * @param input
     * @return
     */
    @GetMapping("/get_data_list")
    public List<DeviceDataModel> getDeviceDataList(AliDeviceDataQueryInputDto input) {
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", input.getAccessKeyId());
        properties.setProperty("accessKeySecret", input.getAccessKeySecret());
        properties.setProperty("endpoint", input.getEndpoint());
        aliApplication.config(properties);

        DeviceInfoModel device = new DeviceInfoModel();
        device.setName(input.getDeviceName());
        device.setProductId(input.getProductId());

        List<DeviceDataModel> list = aliApplication.getDeviceData(device);
        return list;
    }
}
