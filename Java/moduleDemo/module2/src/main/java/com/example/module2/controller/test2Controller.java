package com.example.module2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2022/6/14
 */

@RestController
@RequestMapping("/test2")
public class test2Controller {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test2() {
        return "test2";
    }

}
