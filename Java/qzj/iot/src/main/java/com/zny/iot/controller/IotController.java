package com.zny.iot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2022/8/31
 */

@RestController
@RequestMapping("/iot")
public class IotController {

    @Value("${iot_username}")
    private String name;

    @Value("${iot_password}")
    private String password;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String Test() {
        return "iot";
    }
}
