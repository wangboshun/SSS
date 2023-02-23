package com.wbs.iot.application;

import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;

import java.util.List;
import java.util.Map;

public interface IotInterface {

    /**
     * 获取所有产品
     */
    public List<ProductInfoModel> getProductList();

    /**
     * 获取所有设备
     *
     * @param productId
     */
    public List<DeviceInfoModel> getDeviceList(String productId);

    /**
     * 获取设备数据
     *
     * @param deviceId
     */
    public List<DeviceDataModel> getDeviceData(String deviceId);

    /**
     * 获取设备数据，多参数
     *
     * @param param
     */
    public List<DeviceDataModel> getDeviceData(Map<String, String> param);
}
