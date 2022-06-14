package com.example.module3;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2022/6/14
 */

@RestController
@RequestMapping("/test3")
public class test3Controller {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test3() {
        return "test3";
    }

}
