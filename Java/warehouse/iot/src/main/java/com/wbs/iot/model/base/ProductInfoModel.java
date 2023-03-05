package com.wbs.iot.model.base;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption ProductInfoModel
 */
public class ProductInfoModel implements Serializable {
    private String name;
    private String id;
    private String apiKey;
    private LocalDateTime createTime;

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
