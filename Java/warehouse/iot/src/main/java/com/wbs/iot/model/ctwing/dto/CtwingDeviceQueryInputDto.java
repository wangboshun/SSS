package com.wbs.iot.model.ctwing.dto;

public class CtwingDeviceQueryInputDto extends CtwingAuthDto {
    public String productId;
    private String productApiKey;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductApiKey() {
        return productApiKey;
    }

    public void setProductApiKey(String productApiKey) {
        this.productApiKey = productApiKey;
    }
}
