package com.zny.iot.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Equipment
 */

@TableName("Equipment")
public class EquipmentModel implements Serializable {

    @TableId
     private Integer stationId;
    /**
     *
     */
    private String sx;
    /**
     *
     */
    private String riverName;
    /**
     *
     */
    private String userAddress;
    /**
     *
     */
    private BigDecimal dj;
    /**
     *
     */
    private BigDecimal bw;
    /**
     *
     */
    private String tel;
    /**
     *
     */
    private String settings;
    /**
     *
     */
    private Integer commTypeId;
    /**
     *
     */
    private Integer centerPortId;
    /**
     *
     */
    private String userTel;
    /**
     *
     */
    private String addressPic;
    /**
     *
     */
    private Double height;
    /**
     *
     */
    private String adcd;

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
    public String getSx() {
        return sx;
    }

    /**
     *
     */
    public void setSx(String sx) {
        this.sx = sx;
    }

    /**
     *
     */
    public String getRiverName() {
        return riverName;
    }

    /**
     *
     */
    public void setRiverName(String riverName) {
        this.riverName = riverName;
    }

    /**
     *
     */
    public String getUserAddress() {
        return userAddress;
    }

    /**
     *
     */
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /**
     *
     */
    public BigDecimal getDj() {
        return dj;
    }

    /**
     *
     */
    public void setDj(BigDecimal dj) {
        this.dj = dj;
    }

    /**
     *
     */
    public BigDecimal getBw() {
        return bw;
    }

    /**
     *
     */
    public void setBw(BigDecimal bw) {
        this.bw = bw;
    }

    /**
     *
     */
    public String getTel() {
        return tel;
    }

    /**
     *
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     *
     */
    public String getSettings() {
        return settings;
    }

    /**
     *
     */
    public void setSettings(String settings) {
        this.settings = settings;
    }

    /**
     *
     */
    public Integer getCommTypeId() {
        return commTypeId;
    }

    /**
     *
     */
    public void setCommTypeId(Integer commTypeId) {
        this.commTypeId = commTypeId;
    }

    /**
     *
     */
    public Integer getCenterPortId() {
        return centerPortId;
    }

    /**
     *
     */
    public void setCenterPortId(Integer centerPortId) {
        this.centerPortId = centerPortId;
    }

    /**
     *
     */
    public String getUserTel() {
        return userTel;
    }

    /**
     *
     */
    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    /**
     *
     */
    public String getAddressPic() {
        return addressPic;
    }

    /**
     *
     */
    public void setAddressPic(String addressPic) {
        this.addressPic = addressPic;
    }

    /**
     *
     */
    public Double getHeight() {
        return height;
    }

    /**
     *
     */
    public void setHeight(Double height) {
        this.height = height;
    }

    /**
     *
     */
    public String getAdcd() {
        return adcd;
    }

    /**
     *
     */
    public void setAdcd(String adcd) {
        this.adcd = adcd;
    }
}