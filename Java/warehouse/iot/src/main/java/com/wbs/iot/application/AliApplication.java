package com.wbs.iot.application;

import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
    public void getProductList() {
        try {
            QueryProductListRequest request = new QueryProductListRequest().setPageSize(200).setCurrentPage(1);
            RuntimeOptions runtime = new RuntimeOptions();
            QueryProductListResponse response = client.queryProductListWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryProductListResponseBody.QueryProductListResponseBodyDataListProductInfo> productList = response.getBody().getData().getList().getProductInfo();
                for (QueryProductListResponseBody.QueryProductListResponseBodyDataListProductInfo item : productList) {
                    System.out.print("productName:" + item.getProductName() + " , ");
                    System.out.print("productKey:" + item.getProductKey() + " , ");
                    System.out.println("deviceCount:" + item.getDeviceCount());
                    getDeviceList(item.getProductKey());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void getDeviceList(String productId) {
        try {
            QueryDeviceRequest request = new QueryDeviceRequest().setProductKey(productId);
            com.aliyun.teautil.models.RuntimeOptions runtime = new RuntimeOptions();
            QueryDeviceResponse response = client.queryDeviceWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo> deviceList = response.getBody().getData().getDeviceInfo();
                for (QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo item : deviceList) {
                    System.out.print("deviceId:" + item.getDeviceId() + " , ");
                    System.out.print("deviceName:" + item.getDeviceName() + " , ");
                    System.out.print("deviceStatus:" + item.getDeviceStatus() + " , ");
                    System.out.println("deviceSecret:" + item.getDeviceSecret());
                    Map<String, String> param = new HashMap<>(2);
                    param.put("productId", productId);
                    param.put("deviceName", item.getDeviceName());
                    getDeviceData(param);
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void getDeviceData(String deviceId) {

    }

    @Override
    public void getDeviceData(Map<String, String> param) {
        try {
            QueryDevicePropertyStatusRequest request = new QueryDevicePropertyStatusRequest().setProductKey(param.get("productId")).setDeviceName(param.get("deviceName"));
            RuntimeOptions runtime = new RuntimeOptions();
            QueryDevicePropertyStatusResponse response = client.queryDevicePropertyStatusWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryDevicePropertyStatusResponseBody.QueryDevicePropertyStatusResponseBodyDataListPropertyStatusInfo> propertyList = response.getBody().getData().getList().getPropertyStatusInfo();
                for (QueryDevicePropertyStatusResponseBody.QueryDevicePropertyStatusResponseBodyDataListPropertyStatusInfo item : propertyList) {
                    System.out.print("name:" + item.getName() + " , ");
                    System.out.print("value:" + item.getValue() + " , ");
                    System.out.print("unit:" + item.getUnit() + " , ");
                    System.out.print("time:" + item.getTime() + " , ");
                    System.out.println("identifier:" + item.getIdentifier());
                }
            }

        } catch (Exception e) {

        }
    }
}
