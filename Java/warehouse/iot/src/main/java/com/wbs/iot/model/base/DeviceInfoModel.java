package com.wbs.iot.model.base;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption DeviceInfoModel
 */
public class DeviceInfoModel implements Serializable {
    private String name;
    private String id;
    private int status;
    private String productId;
    private LocalDateTime createTime;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
