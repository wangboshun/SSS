package com.wbs.iot.controller;

import com.wbs.iot.application.CtWingApplication;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import com.wbs.iot.model.ctwing.dto.CtwingAuthDto;
import com.wbs.iot.model.ctwing.dto.CtwingDeviceDataQueryInputDto;
import com.wbs.iot.model.ctwing.dto.CtwingDeviceQueryInputDto;
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
 * @desciption CtwingController
 */

@RestController
@RequestMapping("/iot/ctwing")
@Tag(name = "iot", description = "iot模块")
public class CtwingController {
    private CtWingApplication ctwingApplication;

    public CtwingController(CtWingApplication ctwingApplication) {
        this.ctwingApplication = ctwingApplication;
    }

    /**
     * 获取所有产品
     *
     * @param input
     * @return
     */
    @GetMapping("/get_product_list")
    public List<ProductInfoModel> getProductList(CtwingAuthDto input) {
        Properties properties = new Properties();
        properties.setProperty("appKey", input.getAppKey());
        properties.setProperty("appSecret", input.getAppSecret());
        ctwingApplication.config(properties);
        List<ProductInfoModel> list = ctwingApplication.getProductList();
        return list;
    }

    /**
     * 获取所有设备
     *
     * @param input
     * @return
     */
    @GetMapping("/get_deivce_list")
    public List<DeviceInfoModel> getDeviceList(CtwingAuthDto input) {
        Properties properties = new Properties();
        properties.setProperty("appKey", input.getAppKey());
        properties.setProperty("appSecret", input.getAppSecret());
        ctwingApplication.config(properties);
        List<ProductInfoModel> productList = ctwingApplication.getProductList();

        List<DeviceInfoModel> list = new ArrayList<>();
        for (ProductInfoModel product : productList) {
            List<DeviceInfoModel> deviceList = ctwingApplication.getDeviceList(product);
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
    public List<DeviceInfoModel> getDeviceListByProduct(CtwingDeviceQueryInputDto input) {
        Properties properties = new Properties();
        properties.setProperty("appKey", input.getAppKey());
        properties.setProperty("appSecret", input.getAppSecret());
        ctwingApplication.config(properties);

        ProductInfoModel product = new ProductInfoModel();
        product.setId(input.getProductId());
        product.setApiKey(input.getProductApiKey());

        List<DeviceInfoModel> list = ctwingApplication.getDeviceList(product);
        return list;
    }

    /**
     * 获取指定设备当前数据
     *
     * @param input
     * @return
     */
    @GetMapping("/get_data_list")
    public List<DeviceDataModel> getDeviceData(CtwingDeviceDataQueryInputDto input) {
        Properties properties = new Properties();
        properties.setProperty("appKey", input.getAppKey());
        properties.setProperty("appSecret", input.getAppSecret());
        ctwingApplication.config(properties);

        DeviceInfoModel device = new DeviceInfoModel();
        device.setId(input.getDeviceId());
        device.setProductId(input.getProductId());

        List<DeviceDataModel> list = ctwingApplication.getDeviceData(device);
        return list;
    }
}
