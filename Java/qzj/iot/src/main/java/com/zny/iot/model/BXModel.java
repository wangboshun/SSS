package com.zny.iot.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * BX
 */

@TableName("BX")
public class BXModel implements Serializable {

    @TableId
    private Integer stationId;
    /**
     *
     */
    private String stcd;
    /**
     *
     */
    private Double p;
    /**
     *
     */
    private Integer ph;
    /**
     *
     */
    private Double maxp;
    /**
     *
     */
    private Integer maxph;
    /**
     *
     */
    private Double z;
    /**
     *
     */
    private Integer zh;
    /**
     *
     */
    private Double zmax;
    /**
     *
     */
    private Integer zmaxh;
    /**
     *
     */
    private String bxstcd;
    /**
     *
     */
    private Boolean isbx;
    /**
     *
     */
    private Boolean iszbx;
    /**
     *
     */
    private Boolean iszr;
    /**
     *
     */
    private Boolean isq;
    /**
     *
     */
    private Boolean isyx;

    /**
     *
     */
    public Integer getStationId() {
        return stationId;
    }

    /**
     *
     */
    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    /**
     *
     */
    public String getStcd() {
        return stcd;
    }

    /**
     *
     */
    public void setStcd(String stcd) {
        this.stcd = stcd;
    }

    /**
     *
     */
    public Double getP() {
        return p;
    }

    /**
     *
     */
    public void setP(Double p) {
        this.p = p;
    }

    /**
     *
     */
    public Integer getPh() {
        return ph;
    }

    /**
     *
     */
    public void setPh(Integer ph) {
        this.ph = ph;
    }

    /**
     *
     */
    public Double getMaxp() {
        return maxp;
    }

    /**
     *
     */
    public void setMaxp(Double maxp) {
        this.maxp = maxp;
    }

    /**
     *
     */
    public Integer getMaxph() {
        return maxph;
    }

    /**
     *
     */
    public void setMaxph(Integer maxph) {
        this.maxph = maxph;
    }

    /**
     *
     */
    public Double getZ() {
        return z;
    }

    /**
     *
     */
    public void setZ(Double z) {
        this.z = z;
    }

    /**
     *
     */
    public Integer getZh() {
        return zh;
    }

    /**
     *
     */
    public void setZh(Integer zh) {
        this.zh = zh;
    }

    /**
     *
     */
    public Double getZmax() {
        return zmax;
    }

    /**
     *
     */
    public void setZmax(Double zmax) {
        this.zmax = zmax;
    }

    /**
     *
     */
    public Integer getZmaxh() {
        return zmaxh;
    }

    /**
     *
     */
    public void setZmaxh(Integer zmaxh) {
        this.zmaxh = zmaxh;
    }

    /**
     *
     */
    public String getBxstcd() {
        return bxstcd;
    }

    /**
     *
     */
    public void setBxstcd(String bxstcd) {
        this.bxstcd = bxstcd;
    }

    /**
     *
     */
    public Boolean getIsbx() {
        return isbx;
    }

    /**
     *
     */
    public void setIsbx(Boolean isbx) {
        this.isbx = isbx;
    }

    /**
     *
     */
    public Boolean getIszbx() {
        return iszbx;
    }

    /**
     *
     */
    public void setIszbx(Boolean iszbx) {
        this.iszbx = iszbx;
    }

    /**
     *
     */
    public Boolean getIszr() {
        return iszr;
    }

    /**
     *
     */
    public void setIszr(Boolean iszr) {
        this.iszr = iszr;
    }

    /**
     *
     */
    public Boolean getIsq() {
        return isq;
    }

    /**
     *
     */
    public void setIsq(Boolean isq) {
        this.isq = isq;
    }

    /**
     *
     */
    public Boolean getIsyx() {
        return isyx;
    }

    /**
     *
     */
    public void setIsyx(Boolean isyx) {
        this.isyx = isyx;
    }
}