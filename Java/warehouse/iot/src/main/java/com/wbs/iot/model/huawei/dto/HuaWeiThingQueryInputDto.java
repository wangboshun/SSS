package com.wbs.iot.model.huawei.dto;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption HuaWeiThingQueryInputDto
 */
public class HuaWeiThingQueryInputDto extends HuaWeiAuthDto {
    public String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
