package com.wbs.common.database.base;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WBS
 * @date 2023/3/3 8:57
 * @desciption DataRow
 */
public class DataRow implements Serializable {
    private Map<String, Object> data = new ConcurrentHashMap<>();

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setValue(String column, Object value) {
        if (value != null) {
            this.data.put(column, value);
        }
    }

    public Object getValue(String column) {
        return this.data.get(column);
    }
}
