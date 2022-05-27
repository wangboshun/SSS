package com.zny.quality.check.algorithm;

import com.zny.quality.check.DataCheckHandler;
import com.zny.quality.check.ErrorEnum;

import java.math.BigDecimal;

public class threeAlgorithmHandler extends DataCheckHandler {

    public threeAlgorithmHandler() {
        this.currentValue=new BigDecimal("15");
    }

    @Override
    protected void addWarn(BigDecimal value, ErrorEnum error) {
        System.out.println("已使用 threeAlgorithmHandler 判断为：" +value+" " + error + " " + this.currentValue);
    }
}
