package com.wbs.iot.model.ali.dto;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption AliDeviceQueryInputDto
 */
public class AliDeviceQueryInputDto extends AliAuthDto {
    public String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
