package com.wbs.iot.application;

import com.huaweicloud.sdk.core.auth.AbstractCredentials;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.*;
import com.huaweicloud.sdk.iotda.v5.region.IoTDARegion;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author WBS
 * @date 2023/2/23 14:38
 * @desciption HuaWeiApplication
 */
public class HuaWeiApplication {
    public static IoTDAClient client;

    public static void main(String[] args) {
        createClient();
        queryProductList();
    }

    /**
     * 创建认证客户端
     */
    public static void createClient() {
        String ak = "WPC9R5I3AQAYINPQRAEG";
        String sk = "yyayWWTluTbsF45Q0xWhDPAUZZng8pyxPa1114NW";
        ICredential auth = new BasicCredentials().withDerivedPredicate(AbstractCredentials.DEFAULT_DERIVED_PREDICATE).withAk(ak).withSk(sk);
        client = IoTDAClient.newBuilder().withCredential(auth).withRegion(IoTDARegion.valueOf("cn-north-4")).build();
    }

    /**
     * 查询设备属性值
     *
     * @param deviceId
     */
    public static void queryDevicePropertyStatus(String deviceId) {
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
    }

    /**
     * 查询产品下所有设备
     *
     * @param productId
     */
    public static void queryDeviceByProduct(String productId) {
        ListDevicesRequest request = new ListDevicesRequest();
        request.withProductId(productId);
        try {
            ListDevicesResponse response = client.listDevices(request);
            List<QueryDeviceSimplify> deviceList = response.getDevices();
            for (QueryDeviceSimplify item : deviceList) {
                System.out.print("deviceName:" + item.getDeviceName() + " , ");
                System.out.print("deviceId:" + item.getDeviceId() + "  ");
                queryDevicePropertyStatus(item.getDeviceId());
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 查询所有产品
     */
    public static void queryProductList() {
        ListProductsRequest request = new ListProductsRequest();
        try {
            ListProductsResponse response = client.listProducts(request);
            List<ProductSummary> productList = response.getProducts();
            for (ProductSummary item : productList) {
                System.out.print("productName:" + item.getName() + " , ");
                System.out.print("productId:" + item.getProductId() + "  ");
                queryDeviceByProduct(item.getProductId());
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
