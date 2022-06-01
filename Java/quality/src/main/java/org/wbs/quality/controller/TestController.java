package org.wbs.quality.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2022/6/1
 */

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping(value = "/get")
    public String get() {
        return "get";
    }

    @PostMapping(value = "/post")
    public String post(){
        return "post";
    }
}
