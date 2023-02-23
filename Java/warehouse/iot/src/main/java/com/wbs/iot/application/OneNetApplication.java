package com.wbs.iot.application;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import com.wbs.iot.model.onenet.DeviceDataResult;
import com.wbs.iot.model.onenet.DeviceListResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/2/23 15:34
 * @desciption OneNetApplication
 */
@Component
public class OneNetApplication implements IotInterface {

    public String master_key;

    public OneNetApplication(@Value("${onenet.masterKey}") String masterKey) {
        master_key = masterKey;
    }

    @Override
    public List<ProductInfoModel> getProductList() {
        return null;
    }

    @Override
    public List<DeviceInfoModel> getDeviceList(String productId) {
        List<DeviceInfoModel> list = new ArrayList<>();
        String url = "http://api.heclouds.com/devices";
        String result = HttpRequest.get(url).header("api-key", master_key).execute().body();
        DeviceListResult data = JSONUtil.toBean(result, DeviceListResult.class);
        for (DeviceListResult.Devices item : data.getData().getDevices()) {
            DeviceInfoModel model = new DeviceInfoModel();
            model.setId(item.getId());
            model.setName(item.getTitle());
            model.setStatus(item.getOnline() + "");
            list.add(model);
        }
        return list;
    }

    @Override
    public List<DeviceDataModel> getDeviceData(String deviceId) {
        List<DeviceDataModel> list = new ArrayList<>();
        String url = "http://api.heclouds.com/devices/datapoints?devIds=" + deviceId;
        String result = HttpRequest.get(url).header("api-key", master_key).execute().body();
        DeviceDataResult data = JSONUtil.toBean(result, DeviceDataResult.class);
        for (DeviceDataResult.Devices device : data.getData().getDevices()) {
            for (DeviceDataResult.Datastreams item : device.getDatastreams()) {
                DeviceDataModel model = new DeviceDataModel();
                model.setName(item.getId());
                model.setValue(item.getValue());
                model.setTime(item.getAt() + "");
                list.add(model);
            }
        }
        return list;
    }

    @Override
    public List<DeviceDataModel> getDeviceData(Map<String, String> param) {
        return null;
    }
}
