package com.wbs.iot.model.ctwing.dto;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption CtwingDeviceQueryInputDto
 */
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
