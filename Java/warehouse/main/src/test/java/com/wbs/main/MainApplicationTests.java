package com.wbs.main;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wbs.iot.application.AliApplication;
import com.wbs.iot.application.CtWingApplication;
import com.wbs.iot.application.HuaWeiApplication;
import com.wbs.iot.application.OneNetApplication;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Properties;

@SpringBootTest
class MainApplicationTests {

    @Autowired
    private AliApplication aliApplication;

    @Autowired
    private HuaWeiApplication huaWeiApplication;

    @Autowired
    private OneNetApplication oneNetApplication;

    @Autowired
    private CtWingApplication ctWingApplication;

    @Test
    void ali() {
        JSONConfig jsonConfig = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", "LTAImYBSVfLTwohp");
        properties.setProperty("accessKeySecret", "nac27NrdgEllUXGLw0DXeQOg1zUOJ9");
        properties.setProperty("endpoint", "iot.cn-shanghai.aliyuncs.com");
        aliApplication.config(properties);
        List<ProductInfoModel> productList = aliApplication.getProductList();
        for (ProductInfoModel productInfoModel : productList) {
            List<DeviceInfoModel> deviceList = aliApplication.getDeviceList(productInfoModel);
            for (DeviceInfoModel deviceInfoModel : deviceList) {
                List<DeviceDataModel> dataModelList = aliApplication.getDeviceData(deviceInfoModel);
                for (DeviceDataModel deviceDataModel : dataModelList) {
                    JSONObject object = new JSONObject(deviceDataModel, jsonConfig);
                    System.out.println(JSONUtil.toJsonPrettyStr(object));
                    System.out.println("------------------");
                }
            }
        }
    }

    @Test
    void huawei() {
        Properties properties = new Properties();
        properties.setProperty("accessKeyId", "WPC9R5I3AQAYINPQRAEG");
        properties.setProperty("secretAccessKey", "yyayWWTluTbsF45Q0xWhDPAUZZng8pyxPa1114NW");
        properties.setProperty("region", "cn-north-4");
        huaWeiApplication.config(properties);
        JSONConfig jsonConfig = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProductInfoModel> productList = huaWeiApplication.getProductList();
        for (ProductInfoModel productInfoModel : productList) {
            List<DeviceInfoModel> deviceList = huaWeiApplication.getDeviceList(productInfoModel);
            for (DeviceInfoModel deviceInfoModel : deviceList) {
                List<DeviceDataModel> dataModelList = huaWeiApplication.getDeviceData(deviceInfoModel);
                for (DeviceDataModel deviceDataModel : dataModelList) {
                    JSONObject object = new JSONObject(deviceDataModel, jsonConfig);
                    System.out.println(JSONUtil.toJsonPrettyStr(object));
                    System.out.println("------------------");
                }
            }
        }
    }

    @Test
    void onenet() {
        JSONConfig jsonConfig = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");
        ProductInfoModel product = new ProductInfoModel();
        Properties properties = new Properties();
        properties.setProperty("masterKey", "HxC7tM2w7WuJv0pVJ6Y3LYAKsmY=");
        oneNetApplication.config(properties);
        List<DeviceInfoModel> deviceList = oneNetApplication.getDeviceList(product);
        for (DeviceInfoModel deviceInfoModel : deviceList) {
            List<DeviceDataModel> dataModelList = oneNetApplication.getDeviceData(deviceInfoModel);
            for (DeviceDataModel deviceDataModel : dataModelList) {
                JSONObject object = new JSONObject(deviceDataModel, jsonConfig);
                System.out.println(JSONUtil.toJsonPrettyStr(object));
                System.out.println("------------------");
            }
        }
    }

    @Test
    void cwting() {
        JSONConfig jsonConfig = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");
        Properties properties = new Properties();
        properties.setProperty("appKey", "Pfg5YFp8xQd");
        properties.setProperty("appSecret", "imkBxvny86");
        ctWingApplication.config(properties);
        List<ProductInfoModel> productList = ctWingApplication.getProductList();
        for (ProductInfoModel productInfoModel : productList) {
            List<DeviceInfoModel> deviceList = ctWingApplication.getDeviceList(productInfoModel);
            for (DeviceInfoModel deviceInfoModel : deviceList) {
                List<DeviceDataModel> dataModelList = ctWingApplication.getDeviceData(deviceInfoModel);
                for (DeviceDataModel deviceDataModel : dataModelList) {
                    JSONObject object = new JSONObject(deviceDataModel, jsonConfig);
                    System.out.println(JSONUtil.toJsonPrettyStr(object));
                    System.out.println("------------------");
                }
            }
        }
    }
}
