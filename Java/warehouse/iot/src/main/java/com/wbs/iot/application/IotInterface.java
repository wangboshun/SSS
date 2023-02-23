package com.wbs.iot.application;

import java.util.Map;

public interface IotInterface {

    /**
     * 获取所有产品
     */
    public void getProductList();

    /**
     * 获取所有设备
     *
     * @param productId
     */
    public void getDeviceList(String productId);

    /**
     * 获取设备数据
     *
     * @param deviceId
     */
    public void getDeviceData(String deviceId);

    /**
     * 获取设备数据，多参数
     *
     * @param param
     */
    public void getDeviceData(Map<String, String> param);
}
