package com.wbs.iot.application;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author WBS
 * @date 2023/2/23 17:25
 * @desciption CtWingApplication
 */
@Component
public class CtWingApplication implements IotInterface {
    @Override
    public void getProductList() {

    }

    @Override
    public void getDeviceList(String productId) {

    }

    @Override
    public void getDeviceData(String deviceId) {

    }

    @Override
    public void getDeviceData(Map<String, String> param) {

    }
}
