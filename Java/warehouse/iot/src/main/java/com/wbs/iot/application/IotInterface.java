package com.wbs.iot.application;

import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import com.wbs.iot.model.base.ThingInfoModel;

import java.util.List;
import java.util.Properties;

public interface IotInterface {

    /**
     * 配置信息
     *
     * @param properties 配置
     */
    public void config(Properties properties);

    /**
     * 获取所有产品
     */
    public List<ProductInfoModel> getProductList();

    /**
     * 获取所有设备
     *
     * @param product 产品
     */
    public List<DeviceInfoModel> getDeviceList(ProductInfoModel product);

    /**
     * 获取产品物模型
     *
     * @param product 产品
     */
    public List<ThingInfoModel> getThingInfoList(ProductInfoModel product);

    /**
     * 获取设备数据
     *
     * @param device 设备
     */
    public List<DeviceDataModel> getDeviceData(DeviceInfoModel device);
}
