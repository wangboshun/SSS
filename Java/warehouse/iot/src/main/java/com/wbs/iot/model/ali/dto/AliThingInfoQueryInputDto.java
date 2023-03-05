package com.wbs.iot.model.ali.dto;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption AliThingInfoQueryInputDto
 */
public class AliThingInfoQueryInputDto extends AliAuthDto {
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
