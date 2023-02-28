package com.wbs.iot.model.ali.dto;

public class AliThingInfoQueryInputDto extends AliAuthDto {
    private String productId;
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
