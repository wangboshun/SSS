package com.wbs.main;

import com.wbs.iot.application.AliApplication;
import com.wbs.iot.application.HuaWeiApplication;
import com.wbs.iot.application.OneNetApplication;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MainApplicationTests {

    @Autowired
    private AliApplication aliApplication;

    @Autowired
    private HuaWeiApplication huaWeiApplication;

    @Autowired
    private OneNetApplication oneNetApplication;

    @Test
    void ali() {
        List<ProductInfoModel> productList = aliApplication.getProductList();
        for (ProductInfoModel productInfoModel : productList) {
            List<DeviceInfoModel> deviceList = aliApplication.getDeviceList(productInfoModel.getId());
            for (DeviceInfoModel deviceInfoModel : deviceList) {
                Map<String,String> param=new HashMap<>(2);
                param.put("productId", productInfoModel.getId());
                param.put("deviceName", deviceInfoModel.getName());
                List<DeviceDataModel> dataModelList = aliApplication.getDeviceData(param);
                for (DeviceDataModel deviceDataModel : dataModelList) {

                }
            }
        }
    }

    @Test
    void huawei() {
        List<ProductInfoModel> productList = huaWeiApplication.getProductList();
        for (ProductInfoModel productInfoModel : productList) {
            List<DeviceInfoModel> deviceList = huaWeiApplication.getDeviceList(productInfoModel.getId());
            for (DeviceInfoModel deviceInfoModel : deviceList) {
                List<DeviceDataModel> dataModelList = huaWeiApplication.getDeviceData(deviceInfoModel.getId());
                for (DeviceDataModel deviceDataModel : dataModelList) {

                }
            }
        }
    }

    @Test
    void onenet() {
        List<ProductInfoModel> productList = oneNetApplication.getProductList();
        for (ProductInfoModel productInfoModel : productList) {
            List<DeviceInfoModel> deviceList = oneNetApplication.getDeviceList(productInfoModel.getId());
            for (DeviceInfoModel deviceInfoModel : deviceList) {
                List<DeviceDataModel> dataModelList = oneNetApplication.getDeviceData(deviceInfoModel.getId());
                for (DeviceDataModel deviceDataModel : dataModelList) {

                }
            }
        }
    }

}
