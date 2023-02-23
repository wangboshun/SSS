package com.wbs.iot.application;

import com.wbs.iot.model.base.DeviceDataModel;
import com.wbs.iot.model.base.DeviceInfoModel;
import com.wbs.iot.model.base.ProductInfoModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/2/23 17:25
 * @desciption CtWingApplication
 */
@Component
public class CtWingApplication implements IotInterface {

    @Override
    public List<ProductInfoModel> getProductList() {
        return null;
    }

    @Override
    public List<DeviceInfoModel> getDeviceList(String productId) {
        return null;
    }

    @Override
    public List<DeviceDataModel> getDeviceData(String deviceId) {
        return null;
    }

    @Override
    public List<DeviceDataModel> getDeviceData(Map<String, String> param) {
        return null;
    }
}
