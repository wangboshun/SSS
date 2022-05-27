package com.zny.quality.check.algorithm;

import com.zny.quality.check.DataCheckHandler;
import com.zny.quality.check.ErrorEnum;

import java.math.BigDecimal;

public class oneAlgorithmHandler extends DataCheckHandler {

    public oneAlgorithmHandler() {
        this.currentValue=new BigDecimal("5");
    }

    @Override
    protected void addWarn(BigDecimal value, ErrorEnum error) {
        System.out.println("已使用 oneAlgorithmHandler 判断为：" +value+" " + error + " " + this.currentValue);
    }
}
