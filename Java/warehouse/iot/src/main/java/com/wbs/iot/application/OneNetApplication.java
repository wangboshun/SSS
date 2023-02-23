package com.wbs.iot.application;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.wbs.iot.model.onenet.DeviceDataResult;
import com.wbs.iot.model.onenet.DeviceListResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/2/23 15:34
 * @desciption OneNetApplication
 */
@Component
public class OneNetApplication implements IotInterface {

    public String master_key;

    public OneNetApplication( @Value("${onenet.masterKey}") String masterKey) {
        master_key = masterKey;
    }

    @Override
    public void getProductList() {

    }

    @Override
    public void getDeviceList(String productId) {
        String url = "http://api.heclouds.com/devices";
        String result = HttpRequest.get(url).header("api-key", master_key).execute().body();
        DeviceListResult data = JSONUtil.toBean(result, DeviceListResult.class);
        for (DeviceListResult.Devices item : data.getData().getDevices()) {
            String deviceId = item.getId();
            boolean online = item.getOnline();
            String title = item.getTitle();
            System.out.print("title:" + title + " , ");
            System.out.print("deviceId:" + deviceId + " , ");
            System.out.println("online:" + online + " , ");

            getDeviceData(deviceId);
        }
    }

    @Override
    public void getDeviceData(String deviceId) {
        String url = "http://api.heclouds.com/devices/datapoints?devIds=" + deviceId;
        String result = HttpRequest.get(url).header("api-key", master_key).execute().body();
        DeviceDataResult data = JSONUtil.toBean(result, DeviceDataResult.class);
        for (DeviceDataResult.Devices device : data.getData().getDevices()) {
            for (DeviceDataResult.Datastreams datastream : device.getDatastreams()) {
                String id = datastream.getId();
                String value = datastream.getValue();
                LocalDateTime time = datastream.getAt();
                System.out.print("id:" + id + " , ");
                System.out.print("value:" + value + " , ");
                System.out.println("time:" + time);
            }
        }
    }

    @Override
    public void getDeviceData(Map<String, String> param) {

    }
}
