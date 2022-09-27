package com.zny.iot.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/26
 */

@TableName("StationBaseSet")
public class StationBaseSetModel implements Serializable {

    /**
     *
     */

    @TableId
    private Integer stationid;
    /**
     *
     */
    private String stationname;
    /**
     *
     */
    private Integer regionnum;
    /**
     *
     */
    private Integer stationnum;
    /**
     *
     */
    private Integer parentid;
    /**
     *
     */
    private String slcode;
    /**
     *
     */
    private Integer stationtypeid;
    /**
     *
     */
    private String command;
    /**
     *
     */
    private String timing;
    /**
     *
     */
    private String nextstationid;
    /**
     *
     */
    private Float voltmax;
    /**
     *
     */
    private Float voltmin;
    /**
     *
     */
    private Integer failovertime;
    /**
     *
     */
    private Integer warnovertime;
    /**
     *
     */
    private String remark;

    /**
     *
     */
    public Integer getStationid() {
        return stationid;
    }

    /**
     *
     */
    public void setStationid(Integer stationid) {
        this.stationid = stationid;
    }

    /**
     *
     */
    public String getStationname() {
        return stationname;
    }

    /**
     *
     */
    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    /**
     *
     */
    public Integer getRegionnum() {
        return regionnum;
    }

    /**
     *
     */
    public void setRegionnum(Integer regionnum) {
        this.regionnum = regionnum;
    }

    /**
     *
     */
    public Integer getStationnum() {
        return stationnum;
    }

    /**
     *
     */
    public void setStationnum(Integer stationnum) {
        this.stationnum = stationnum;
    }

    /**
     *
     */
    public Integer getParentid() {
        return parentid;
    }

    /**
     *
     */
    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    /**
     *
     */
    public String getSlcode() {
        return slcode;
    }

    /**
     *
     */
    public void setSlcode(String slcode) {
        this.slcode = slcode;
    }

    /**
     *
     */
    public Integer getStationtypeid() {
        return stationtypeid;
    }

    /**
     *
     */
    public void setStationtypeid(Integer stationtypeid) {
        this.stationtypeid = stationtypeid;
    }

    /**
     *
     */
    public String getCommand() {
        return command;
    }

    /**
     *
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     *
     */
    public String getTiming() {
        return timing;
    }

    /**
     *
     */
    public void setTiming(String timing) {
        this.timing = timing;
    }

    /**
     *
     */
    public String getNextstationid() {
        return nextstationid;
    }

    /**
     *
     */
    public void setNextstationid(String nextstationid) {
        this.nextstationid = nextstationid;
    }

    /**
     *
     */
    public Float getVoltmax() {
        return voltmax;
    }

    /**
     *
     */
    public void setVoltmax(Float voltmax) {
        this.voltmax = voltmax;
    }

    /**
     *
     */
    public Float getVoltmin() {
        return voltmin;
    }

    /**
     *
     */
    public void setVoltmin(Float voltmin) {
        this.voltmin = voltmin;
    }

    /**
     *
     */
    public Integer getFailovertime() {
        return failovertime;
    }

    /**
     *
     */
    public void setFailovertime(Integer failovertime) {
        this.failovertime = failovertime;
    }

    /**
     *
     */
    public Integer getWarnovertime() {
        return warnovertime;
    }

    /**
     *
     */
    public void setWarnovertime(Integer warnovertime) {
        this.warnovertime = warnovertime;
    }

    /**
     *
     */
    public String getRemark() {
        return remark;
    }

    /**
     *
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}