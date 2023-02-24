package com.wbs.iot.application;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import com.wbs.iot.model.onenet.DeviceDataResult;
import com.wbs.iot.model.onenet.DeviceListResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author WBS
 * @date 2023/2/23 15:34
 * @desciption OneNetApplication
 */
@Component
public class OneNetApplication implements IotInterface {

    private String masterKey;

    /**
     * 配置参数，必须要masterKey
     *
     * @param properties
     */
    public void config(Properties properties) {
        masterKey = properties.getProperty("masterKey");
    }

    /**
     * 获取产品列表
     *
     * @return
     */
    @Override
    public List<ProductInfoModel> getProductList() {
        return null;
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
            String url = "http://api.heclouds.com/devices";
            String result = HttpRequest.get(url).header("api-key", masterKey).execute().body();
            DeviceListResult data = JSONUtil.toBean(result, DeviceListResult.class);
            for (DeviceListResult.Devices item : data.getData().getDevices()) {
                DeviceInfoModel model = new DeviceInfoModel();
                model.setId(item.getId());
                model.setName(item.getTitle());
                model.setProductId(product.getId());
                model.setStatus(item.getOnline() + "");
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
            String url = "http://api.heclouds.com/devices/datapoints?devIds=" + device.getId();
            String result = HttpRequest.get(url).header("api-key", masterKey).execute().body();
            DeviceDataResult data = JSONUtil.toBean(result, DeviceDataResult.class);
            for (DeviceDataResult.Devices deviceItem : data.getData().getDevices()) {
                for (DeviceDataResult.Datastreams item : deviceItem.getDatastreams()) {
                    DeviceDataModel model = new DeviceDataModel();
                    model.setName(item.getId());
                    model.setValue(item.getValue());
                    model.setDeviceId(device.getId());
                    model.setTime(item.getAt());
                    list.add(model);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
}
