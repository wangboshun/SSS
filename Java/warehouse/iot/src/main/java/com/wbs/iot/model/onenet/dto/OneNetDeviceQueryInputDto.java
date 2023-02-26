package com.wbs.iot.model.onenet.dto;

public class OneNetDeviceQueryInputDto extends OneNetAuthDto {
    public String productName;
    public String productId;
    private String productApiKey;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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
