package com.wbs.iot.application;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.wbs.iot.model.onenet.DeviceDataResult;
import com.wbs.iot.model.onenet.DeviceListResult;

import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/2/23 15:34
 * @desciption OneNetApplication
 */
public class OneNetApplication {

    public static String master_key = "HxC7tM2w7WuJv0pVJ6Y3LYAKsmY=";

    public static void main(String[] args) {
        queryDeviceList();
    }

    /**
     * 查询设备列表
     */
    public static void queryDeviceList() {
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

            queryDeviceData(deviceId);
        }
    }

    /**
     * 查询指定设备数据流
     *
     * @param deviceId
     */
    public static void queryDeviceData(String deviceId) {
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
}
