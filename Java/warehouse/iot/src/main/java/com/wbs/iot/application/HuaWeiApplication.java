package com.wbs.iot.application;

import com.huaweicloud.sdk.core.auth.AbstractCredentials;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.*;
import com.huaweicloud.sdk.iotda.v5.region.IoTDARegion;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/2/23 14:38
 * @desciption HuaWeiApplication
 */
@Component
public class HuaWeiApplication implements IotInterface {
    public IoTDAClient client;

    public HuaWeiApplication(@Value("${huawei.ak}") String ak, @Value("${huawei.sk}") String sk, @Value("${huawei.region}") String region) {
        try {
            ICredential auth = new BasicCredentials().withDerivedPredicate(AbstractCredentials.DEFAULT_DERIVED_PREDICATE).withAk(ak).withSk(sk);
            client = IoTDAClient.newBuilder().withCredential(auth).withRegion(IoTDARegion.valueOf(region)).build();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public List<ProductInfoModel> getProductList() {
        ListProductsRequest request = new ListProductsRequest();
        List<ProductInfoModel> list = new ArrayList<ProductInfoModel>();
        try {
            ListProductsResponse response = client.listProducts(request);
            List<ProductSummary> productList = response.getProducts();
            for (ProductSummary item : productList) {
                ProductInfoModel model = new ProductInfoModel();
                model.setId(item.getProductId());
                model.setName(item.getName());
                list.add(model);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    @Override
    public List<DeviceInfoModel> getDeviceList(String productId) {
        ListDevicesRequest request = new ListDevicesRequest();
        List<DeviceInfoModel> list = new ArrayList<>();
        request.withProductId(productId);
        try {
            ListDevicesResponse response = client.listDevices(request);
            List<QueryDeviceSimplify> deviceList = response.getDevices();
            for (QueryDeviceSimplify item : deviceList) {
                DeviceInfoModel model = new DeviceInfoModel();
                model.setId(item.getDeviceId());
                model.setName(item.getDeviceName());
                list.add(model);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    @Override
    public List<DeviceDataModel> getDeviceData(String deviceId) {
        try {
            ShowDeviceShadowRequest request = new ShowDeviceShadowRequest();
            request.setDeviceId(deviceId);
            ShowDeviceShadowResponse response = client.showDeviceShadow(request);
            List<DeviceShadowData> shadowList = response.getShadow();
            for (DeviceShadowData item : shadowList) {
                Object properties = item.getReported().getProperties();
                LinkedHashMap<String, Object> map = (LinkedHashMap) properties;
                for (String key : map.keySet()) {
                    System.out.println(key + ":" + map.get(key));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<DeviceDataModel> getDeviceData(Map<String, String> param) {
        return null;
    }
}
