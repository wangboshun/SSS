package com.zny.iot.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/26
 * 测站类
 */

@TableName("StationBaseSet")
public class StationBaseSetModel implements Serializable {
    @TableId
    private Integer stationId;
    /**
     *
     */
    private String stationName;
    /**
     *
     */
    private Integer regionNum;
    /**
     *
     */
    private Integer stationNum;
    /**
     *
     */
    private Integer parentId;
    /**
     *
     */
    private String slCode;
    /**
     *
     */
    private Integer stationTypeId;
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
    private String nextStationId;
    /**
     *
     */
    private Float voltMax;
    /**
     *
     */
    private Float voltMin;
    /**
     *
     */
    private Integer failOverTime;
    /**
     *
     */
    private Integer warnOverTime;
    /**
     *
     */
    private String remark;

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
    public String getStationName() {
        return stationName;
    }

    /**
     *
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    /**
     *
     */
    public Integer getRegionNum() {
        return regionNum;
    }

    /**
     *
     */
    public void setRegionNum(Integer regionNum) {
        this.regionNum = regionNum;
    }

    /**
     *
     */
    public Integer getStationNum() {
        return stationNum;
    }

    /**
     *
     */
    public void setStationNum(Integer stationNum) {
        this.stationNum = stationNum;
    }

    /**
     *
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     *
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     *
     */
    public String getSlCode() {
        return slCode;
    }

    /**
     *
     */
    public void setSlCode(String slCode) {
        this.slCode = slCode;
    }

    /**
     *
     */
    public Integer getStationTypeId() {
        return stationTypeId;
    }

    /**
     *
     */
    public void setStationTypeId(Integer stationTypeId) {
        this.stationTypeId = stationTypeId;
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
    public String getNextStationId() {
        return nextStationId;
    }

    /**
     *
     */
    public void setNextStationId(String nextStationId) {
        this.nextStationId = nextStationId;
    }

    /**
     *
     */
    public Float getVoltMax() {
        return voltMax;
    }

    /**
     *
     */
    public void setVoltMax(Float voltMax) {
        this.voltMax = voltMax;
    }

    /**
     *
     */
    public Float getVoltMin() {
        return voltMin;
    }

    /**
     *
     */
    public void setVoltMin(Float voltMin) {
        this.voltMin = voltMin;
    }

    /**
     *
     */
    public Integer getFailOverTime() {
        return failOverTime;
    }

    /**
     *
     */
    public void setFailOverTime(Integer failOverTime) {
        this.failOverTime = failOverTime;
    }

    /**
     *
     */
    public Integer getWarnOverTime() {
        return warnOverTime;
    }

    /**
     *
     */
    public void setWarnOverTime(Integer warnOverTime) {
        this.warnOverTime = warnOverTime;
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