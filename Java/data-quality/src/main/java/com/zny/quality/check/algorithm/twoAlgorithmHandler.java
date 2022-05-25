package com.zny.quality.check.algorithm;

import com.zny.quality.check.DataCheckHandler;
import com.zny.quality.check.ErrorEnum;

import java.math.BigDecimal;

public class twoAlgorithmHandler extends DataCheckHandler {

    public twoAlgorithmHandler() {
        this.value = new BigDecimal(10);
    }

    @Override
    protected void addWarn(BigDecimal value, ErrorEnum error) {
        System.out.println("已使用 twoAlgorithmHandler 判断为：" + error +" "+ this.value);
    }
}
