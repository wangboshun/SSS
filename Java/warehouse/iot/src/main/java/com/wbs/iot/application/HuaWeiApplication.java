package com.wbs.iot.application;

import com.huaweicloud.sdk.core.auth.AbstractCredentials;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.*;
import com.huaweicloud.sdk.iotda.v5.region.IoTDARegion;
import com.wbs.common.utils.DateUtils;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/2/23 14:38
 * @desciption HuaWeiApplication
 */
@Service
public class HuaWeiApplication implements IotInterface {
    private IoTDAClient client;

    /**
     * 配置参数，accessKeyId、secretAccessKey、region
     *
     * @param properties
     */
    @Override
    public void config(Properties properties) {
        try {
            String ak = properties.getProperty("accessKeyId");
            String sk = properties.getProperty("secretAccessKey");
            String region = properties.getProperty("region");
            ICredential auth = new BasicCredentials().withDerivedPredicate(AbstractCredentials.DEFAULT_DERIVED_PREDICATE).withAk(ak).withSk(sk);
            client = IoTDAClient.newBuilder().withCredential(auth).withRegion(IoTDARegion.valueOf(region)).build();
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
        List<ProductInfoModel> list = new ArrayList<ProductInfoModel>();
        try {
            ListProductsRequest request = new ListProductsRequest();
            ListProductsResponse response = client.listProducts(request);
            List<ProductSummary> productList = response.getProducts();
            for (ProductSummary item : productList) {
                ProductInfoModel model = new ProductInfoModel();
                model.setId(item.getProductId());
                model.setName(item.getName());
                model.setCreateTime(DateUtils.strToDate(item.getCreateTime(), "yyyyMMdd'T'HHmmss'Z'", true));
                list.add(model);
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
            ListDevicesRequest request = new ListDevicesRequest();
            request.setProductId(product.getId());
            ListDevicesResponse response = client.listDevices(request);
            List<QueryDeviceSimplify> deviceList = response.getDevices();
            for (QueryDeviceSimplify item : deviceList) {
                ShowDeviceRequest deviceRequest = new ShowDeviceRequest();
                deviceRequest.setDeviceId(item.getDeviceId());
                ShowDeviceResponse deviceResponse = client.showDevice(deviceRequest);
                DeviceInfoModel model = new DeviceInfoModel();
                model.setId(deviceResponse.getDeviceId());
                model.setProductId(product.getId());
                model.setName(deviceResponse.getDeviceName());
                model.setCreateTime(DateUtils.strToDate(deviceResponse.getCreateTime(), "yyyyMMdd'T'HHmmss'Z'", true));
                list.add(model);
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
            ShowDeviceShadowRequest request = new ShowDeviceShadowRequest();
            request.setDeviceId(device.getId());
            ShowDeviceShadowResponse response = client.showDeviceShadow(request);
            List<DeviceShadowData> shadowList = response.getShadow();
            for (DeviceShadowData item : shadowList) {
                Object properties = item.getReported().getProperties();
                LocalDateTime time = DateUtils.strToDate(item.getReported().getEventTime(), "yyyyMMdd'T'HHmmss'Z'", true);
                LinkedHashMap<String, Object> map = (LinkedHashMap) properties;
                for (String key : map.keySet()) {
                    DeviceDataModel model = new DeviceDataModel();
                    model.setTime(time);
                    model.setName(key);
                    model.setDeviceId(device.getId());
                    model.setValue(map.get(key) + "");
                    list.add(model);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
}
