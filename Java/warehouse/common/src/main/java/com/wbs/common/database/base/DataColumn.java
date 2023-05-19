package com.wbs.common.database.base;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/5/13 22:27
 * @desciption DataColumn
 */
public class DataColumn  implements Serializable {
    private String name;
    private String dataType;
    private boolean isPrimary;


    public DataColumn(){

    }

    public DataColumn(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
        this.isPrimary = false;
    }

    public DataColumn(String name, String dataType, boolean isPrimary) {
        this.name = name;
        this.dataType = dataType;
        this.isPrimary = isPrimary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return this.dataType;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
