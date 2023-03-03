package com.wbs.engine.model;

import java.util.ArrayList;

/**
 * @author WBS
 * @date 2023/3/3 8:58
 * @desciption DataTable
 */
public class DataTable extends ArrayList<DataRow> {

    public DataTable() {

    }

    public DataTable(String name) {
        this.setName(name);
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
