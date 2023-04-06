package com.wbs.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WBS
 * @date 2023/4/6 15:51
 * @desciption BaseModel
 */
public class BaseModel implements Serializable {
    private String id;
    private LocalDateTime ct;
    private LocalDateTime ut;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCt() {
        return ct;
    }

    public void setCt(LocalDateTime ct) {
        this.ct = ct;
    }

    public LocalDateTime getUt() {
        return ut;
    }

    public void setUt(LocalDateTime ut) {
        this.ut = ut;
    }
}
