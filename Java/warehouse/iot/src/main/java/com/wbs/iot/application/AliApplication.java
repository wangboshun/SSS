package com.wbs.iot.application;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.wbs.common.utils.DateUtils;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import com.wbs.iot.model.base.ThingInfoModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/2/23 10:35
 * @desciption AliApplication
 */
@Service
public class AliApplication implements IotInterface {
    private Client client;

    /**
     * 配置参数，必须要accessKeyId、accessKeySecret、endpoint
     *
     * @param properties
     */
    public void config(Properties properties) {
        try {
            String accessKeyId = properties.getProperty("accessKeyId");
            String accessKeySecret = properties.getProperty("accessKeySecret");
            String endpoint = properties.getProperty("endpoint");
            Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
            config.endpoint = endpoint;
            client = new Client(config);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 获取产品列表
     *
     * @return
     */
    @Override
    public List<ProductInfoModel> getProductList() {
        List<ProductInfoModel> list = new ArrayList<>();
        try {
            QueryProductListRequest request = new QueryProductListRequest().setPageSize(200).setCurrentPage(1);
            RuntimeOptions runtime = new RuntimeOptions();
            QueryProductListResponse response = client.queryProductListWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryProductListResponseBody.QueryProductListResponseBodyDataListProductInfo> productList = response.getBody().getData().getList().getProductInfo();
                for (QueryProductListResponseBody.QueryProductListResponseBodyDataListProductInfo item : productList) {
                    ProductInfoModel model = new ProductInfoModel();
                    model.setId(item.getProductKey());
                    model.setApiKey(item.getProductKey());
                    model.setCreateTime(DateUtils.unixToDate(item.getGmtCreate()));
                    model.setName(item.getProductName());
                    list.add(model);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
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
            QueryDeviceRequest request = new QueryDeviceRequest().setProductKey(product.getId());
            com.aliyun.teautil.models.RuntimeOptions runtime = new RuntimeOptions();
            QueryDeviceResponse response = client.queryDeviceWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo> deviceList = response.getBody().getData().getDeviceInfo();
                for (QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo item : deviceList) {
                    DeviceInfoModel model = new DeviceInfoModel();
                    model.setId(item.getDeviceId());
                    model.setProductId(product.getId());
                    model.setName(item.getDeviceName());
                    if ("ONLINE".equals(item.getDeviceStatus())) {
                        model.setStatus(1);
                    } else {
                        model.setStatus(0);
                    }
                    LocalDateTime time = DateUtils.strToDate(item.getUtcCreate(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", true);
                    model.setCreateTime(time);
                    list.add(model);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
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
            GetThingModelTslRequest request = new GetThingModelTslRequest().setProductKey(product.getId());
            RuntimeOptions runtime = new RuntimeOptions();
            GetThingModelTslResponse response = client.getThingModelTslWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                Map<String, Object> tslMap = response.getBody().getData().toMap();
                JSONObject properties = JSONUtil.parseObj(tslMap.get("TslStr"));
                List<JSONObject> jsonArray = new ArrayList<>();
                jsonArray = properties.get("properties", jsonArray.getClass());
                for (JSONObject item : jsonArray) {
                    ThingInfoModel model = new ThingInfoModel();
                    model.setName(item.getStr("name"));
                    model.setProductId(product.getId());
                    model.setProperty(item.getStr("identifier"));

                    JSONObject dataType = JSONUtil.parseObj(item.get("dataType"));
                    model.setDataType(dataType.getStr("type"));
                    if (dataType.get("specs").getClass().toString().contains("JSONArray")) {
                        // TODO 如果是经纬度信息，这里会有三个数据类型和单位
                    } else {
                        JSONObject specs = JSONUtil.parseObj(dataType.get("specs"));
                        if (specs.containsKey("unit")) {
                            model.setUnit(specs.getStr("unit"));
                        }
                    }

                    list.add(model);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
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
            QueryDevicePropertyStatusRequest request = new QueryDevicePropertyStatusRequest().setProductKey(device.getProductId()).setDeviceName(device.getName());
            RuntimeOptions runtime = new RuntimeOptions();
            QueryDevicePropertyStatusResponse response = client.queryDevicePropertyStatusWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryDevicePropertyStatusResponseBody.QueryDevicePropertyStatusResponseBodyDataListPropertyStatusInfo> propertyList = response.getBody().getData().getList().getPropertyStatusInfo();
                for (QueryDevicePropertyStatusResponseBody.QueryDevicePropertyStatusResponseBodyDataListPropertyStatusInfo item : propertyList) {
                    if (item.getTime() == null) {
                        continue;
                    }
                    DeviceDataModel model = new DeviceDataModel();
                    model.setName(item.getName());
                    model.setValue(item.getValue());
                    model.setTime(DateUtils.unixToDate(item.getTime()));
                    model.setDeviceId(device.getId());
                    model.setProperty(item.getIdentifier());
                    list.add(model);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
}
