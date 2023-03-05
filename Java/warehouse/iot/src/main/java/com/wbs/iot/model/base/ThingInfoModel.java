package com.wbs.iot.model.base;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/2/27 17:47
 * @desciption ThingInfoModel
 */
public class ThingInfoModel implements Serializable {
    private String name;
    private String productId;
    private String dataType;
    private String unit;
    private String property;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
