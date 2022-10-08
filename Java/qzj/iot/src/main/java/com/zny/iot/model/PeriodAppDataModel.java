package com.zny.iot.model;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author WBS
 * Date:2022/9/28
 */

@TableName("PeriodAppData")
public class PeriodAppDataModel {
    public int pointId;
    public String dataTime;
    public float appValue;
    public char sign;

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public float getAppValue() {
        return appValue;
    }

    public void setAppValue(float appValue) {
        this.appValue = appValue;
    }

    public char getSign() {
        return sign;
    }

    public void setSign(char sign) {
        this.sign = sign;
    }
}
