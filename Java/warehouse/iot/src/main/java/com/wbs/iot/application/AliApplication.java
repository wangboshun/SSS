package com.wbs.iot.application;

import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/2/23 10:35
 * @desciption AliApplication
 */
@Component
public class AliApplication implements IotInterface {
    public Client client;

    public AliApplication(@Value("${ali.accessKeyId}") String accessKeyId, @Value("${ali.accessKeySecret}") String accessKeySecret, @Value("${ali.endpoint}") String endpoint) {
        try {
            Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
            config.endpoint = endpoint;
            client = new Client(config);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public List<ProductInfoModel> getProductList() {
        List<ProductInfoModel> list = new ArrayList<ProductInfoModel>();
        try {
            QueryProductListRequest request = new QueryProductListRequest().setPageSize(200).setCurrentPage(1);
            RuntimeOptions runtime = new RuntimeOptions();
            QueryProductListResponse response = client.queryProductListWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryProductListResponseBody.QueryProductListResponseBodyDataListProductInfo> productList = response.getBody().getData().getList().getProductInfo();
                for (QueryProductListResponseBody.QueryProductListResponseBodyDataListProductInfo item : productList) {
                    ProductInfoModel model = new ProductInfoModel();
                    model.setId(item.getProductKey());
                    model.setName(item.getProductName());
                    list.add(model);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    @Override
    public List<DeviceInfoModel> getDeviceList(String productId) {
        List<DeviceInfoModel> list = new ArrayList<>();
        try {
            QueryDeviceRequest request = new QueryDeviceRequest().setProductKey(productId);
            com.aliyun.teautil.models.RuntimeOptions runtime = new RuntimeOptions();
            QueryDeviceResponse response = client.queryDeviceWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo> deviceList = response.getBody().getData().getDeviceInfo();
                for (QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo item : deviceList) {
                    DeviceInfoModel model = new DeviceInfoModel();
                    model.setId(item.getDeviceId());
                    model.setName(item.getDeviceName());
                    model.setStatus(item.getDeviceStatus());
                    list.add(model);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    @Override
    public List<DeviceDataModel> getDeviceData(String deviceId) {
        return null;
    }

    @Override
    public List<DeviceDataModel> getDeviceData(Map<String, String> param) {
        List<DeviceDataModel> list = new ArrayList<>();
        try {
            QueryDevicePropertyStatusRequest request = new QueryDevicePropertyStatusRequest().setProductKey(param.get("productId")).setDeviceName(param.get("deviceName"));
            RuntimeOptions runtime = new RuntimeOptions();
            QueryDevicePropertyStatusResponse response = client.queryDevicePropertyStatusWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryDevicePropertyStatusResponseBody.QueryDevicePropertyStatusResponseBodyDataListPropertyStatusInfo> propertyList = response.getBody().getData().getList().getPropertyStatusInfo();
                for (QueryDevicePropertyStatusResponseBody.QueryDevicePropertyStatusResponseBodyDataListPropertyStatusInfo item : propertyList) {
                    DeviceDataModel model = new DeviceDataModel();
                    model.setName(item.getName());
                    model.setValue(item.getValue());
                    model.setTime(item.getTime());
                    list.add(model);
                }
            }

        } catch (Exception e) {

        }
        return list;
    }
}
