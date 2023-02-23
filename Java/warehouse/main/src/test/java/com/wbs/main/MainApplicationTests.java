package com.wbs.main;

import com.wbs.iot.application.AliApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainApplicationTests {

    @Autowired
    private AliApplication aliApplication;

    @Test
    void contextLoads() {
        aliApplication.getProductList();
    }

}
