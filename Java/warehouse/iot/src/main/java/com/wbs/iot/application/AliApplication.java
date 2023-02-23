package com.wbs.iot.application;

import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

import java.util.List;

/**
 * @author WBS
 * @date 2023/2/23 10:35
 * @desciption AliApplication
 */
public class AliApplication {
    public static Client client;

    public static void main(String[] args) {
        try {
            createClient();
            queryProductList();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 创建认证客户端
     */
    public static void createClient() throws Exception {
        String accessKeyId = "LTAImYBSVfLTwohp";
        String accessKeySecret = "nac27NrdgEllUXGLw0DXeQOg1zUOJ9";
        Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
        config.endpoint = "iot.cn-shanghai.aliyuncs.com";
        client = new Client(config);
    }

    /**
     * 查询设备属性值
     *
     * @param productKey
     * @param deviceName
     */
    public static void queryDevicePropertyStatus(String productKey, String deviceName) {
        try {
            QueryDevicePropertyStatusRequest request = new QueryDevicePropertyStatusRequest().setProductKey(productKey).setDeviceName(deviceName);
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

    /**
     * 查询产品下所有设备
     *
     * @param productKey
     */
    public static void queryDeviceByProduct(String productKey) {
        try {
            QueryDeviceRequest request = new QueryDeviceRequest().setProductKey(productKey);
            com.aliyun.teautil.models.RuntimeOptions runtime = new RuntimeOptions();
            QueryDeviceResponse response = client.queryDeviceWithOptions(request, runtime);
            if (response != null && response.getStatusCode() == 200) {
                List<QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo> deviceList = response.getBody().getData().getDeviceInfo();
                for (QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo item : deviceList) {
                    System.out.print("deviceId:" + item.getDeviceId() + " , ");
                    System.out.print("deviceName:" + item.getDeviceName() + " , ");
                    System.out.print("deviceStatus:" + item.getDeviceStatus() + " , ");
                    System.out.println("deviceSecret:" + item.getDeviceSecret());
                    queryDevicePropertyStatus(productKey, item.getDeviceName());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 查询所有产品
     */
    public static void queryProductList() {
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
                    queryDeviceByProduct(item.getProductKey());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
