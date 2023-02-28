package com.wbs.iot.model.huawei.dto;

public class HuaWeiThingQueryInputDto extends HuaWeiAuthDto {
    public String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
