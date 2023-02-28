package com.wbs.iot.application;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ctg.ag.sdk.biz.AepDeviceManagementClient;
import com.ctg.ag.sdk.biz.AepDeviceModelClient;
import com.ctg.ag.sdk.biz.AepDeviceStatusClient;
import com.ctg.ag.sdk.biz.AepProductManagementClient;
import com.ctg.ag.sdk.biz.aep_device_management.QueryDeviceListRequest;
import com.ctg.ag.sdk.biz.aep_device_management.QueryDeviceListResponse;
import com.ctg.ag.sdk.biz.aep_device_model.QueryPropertyListRequest;
import com.ctg.ag.sdk.biz.aep_device_model.QueryPropertyListResponse;
import com.ctg.ag.sdk.biz.aep_device_status.QueryDeviceStatusListRequest;
import com.ctg.ag.sdk.biz.aep_device_status.QueryDeviceStatusListResponse;
import com.ctg.ag.sdk.biz.aep_product_management.QueryProductListRequest;
import com.ctg.ag.sdk.biz.aep_product_management.QueryProductListResponse;
import com.wbs.common.utils.DateUtils;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import com.wbs.iot.model.base.ThingInfoModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/2/23 17:25
 * @desciption CtWingApplication
 */
@Service
public class CtWingApplication implements IotInterface {

    private String appKey;
    private String appSecret;

    /**
     * 配置参数，必须要appKey、appSecret
     *
     * @param properties
     */
    public void config(Properties properties) {
        appKey = properties.getProperty("appKey");
        appSecret = properties.getProperty("appSecret");
    }

    /**
     * 获取产品列表
     *
     * @return
     */
    @Override
    public List<ProductInfoModel> getProductList() {
        List<ProductInfoModel> list = new ArrayList<ProductInfoModel>();
        try {
            AepProductManagementClient client = AepProductManagementClient.newClient().appKey(appKey).appSecret(appSecret).build();
            QueryProductListRequest request = new QueryProductListRequest();
            QueryProductListResponse response = client.QueryProductList(request);
            JSONObject json = JSONUtil.parseObj(new String(response.getBody()));
            JSONArray array = json.getByPath("result.list", JSONArray.class);
            List<Dict> dictList = JSONUtil.toList(array, Dict.class);
            for (Dict item : dictList) {
                ProductInfoModel model = new ProductInfoModel();
                model.setId(item.getStr("productId"));
                model.setName(item.getStr("productName"));
                model.setApiKey(item.getStr("apiKey"));
                model.setCreateTime(DateUtils.unixToDate(item.getStr("createTime")));
                list.add(model);
            }
            System.out.println();
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * 获取设备列表
     *
     * @param product
     * @return
     */
    @Override
    public List<DeviceInfoModel> getDeviceList(ProductInfoModel product) {
        List<DeviceInfoModel> list = new ArrayList<>();
        try {
            AepDeviceManagementClient client = AepDeviceManagementClient.newClient().appKey(appKey).appSecret(appSecret).build();
            QueryDeviceListRequest request = new QueryDeviceListRequest();
            request.setParamMasterKey(product.getApiKey());
            request.setParamProductId(product.getId());
            QueryDeviceListResponse response = client.QueryDeviceList(request);
            JSONObject json = JSONUtil.parseObj(new String(response.getBody()));
            JSONArray array = json.getByPath("result.list", JSONArray.class);
            List<Dict> dictList = JSONUtil.toList(array, Dict.class);
            for (Dict item : dictList) {
                DeviceInfoModel model = new DeviceInfoModel();
                model.setId(item.getStr("deviceId"));
                model.setName(item.getStr("deviceName"));
                model.setProductId(item.getStr("productId"));
                if ("1".equals(item.getStr("netStatus"))) {
                    model.setStatus(1);
                } else {
                    model.setStatus(0);
                }
                model.setCreateTime(DateUtils.unixToDate(item.getStr("createTime")));
                list.add(model);
            }
            System.out.println("");
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * 获取产品物模型
     *
     * @param product 产品
     * @return
     */
    @Override
    public List<ThingInfoModel> getThingInfoList(ProductInfoModel product) {
        List<ThingInfoModel> list = new ArrayList<>();
        try {
            AepDeviceModelClient client = AepDeviceModelClient.newClient().appKey(appKey).appSecret(appSecret).build();
            QueryPropertyListRequest request = new QueryPropertyListRequest();
            request.setParamMasterKey(product.getApiKey());
            request.setParamProductId(product.getId());
            QueryPropertyListResponse response = client.QueryPropertyList(request);
            JSONObject json = JSONUtil.parseObj(new String(response.getBody()));
            JSONArray array = json.getByPath("result.list", JSONArray.class);
            List<Dict> dictList = JSONUtil.toList(array, Dict.class);
            for (Dict item : dictList) {
                ThingInfoModel model = new ThingInfoModel();
                model.setName(item.getStr("propertyName"));
                model.setProductId(product.getId());
                model.setProperty(item.getStr("propertyFlag") );
                model.setDataType(item.getStr("dataType"));
                model.setUnit(item.getStr("unit"));
                list.add(model);
            }

        } catch (Exception e) {

        }
        return list;
    }

    /**
     * 获取设备数据
     *
     * @param device
     * @return
     */
    @Override
    public List<DeviceDataModel> getDeviceData(DeviceInfoModel device) {
        List<DeviceDataModel> list = new ArrayList<>();
        try {
            AepDeviceStatusClient client = AepDeviceStatusClient.newClient().appKey(appKey).appSecret(appSecret).build();
            String body = "{\"productId\":\"" + device.getProductId() + "\",\"deviceId\":\"" + device.getId() + "\"}";
            QueryDeviceStatusListRequest request = new QueryDeviceStatusListRequest();
            request.setBody(body.getBytes());
            QueryDeviceStatusListResponse response = client.QueryDeviceStatusList(request);
            JSONObject json = JSONUtil.parseObj(new String(response.getBody()));
            JSONArray array = json.getByPath("deviceStatusList", JSONArray.class);
            List<Dict> dictList = JSONUtil.toList(array, Dict.class);
            for (Dict item : dictList) {
                DeviceDataModel model = new DeviceDataModel();
                model.setName(item.getStr("datasetId"));
                model.setValue(item.getStr("value"));
                model.setDeviceId(device.getId());
                model.setTime(DateUtils.unixToDate(item.getStr("timestamp")));
                list.add(model);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
}
