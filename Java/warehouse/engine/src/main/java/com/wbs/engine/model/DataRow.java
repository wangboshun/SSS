package com.wbs.engine.model;

import java.util.HashMap;

/**
 * @author WBS
 * @date 2023/3/3 8:57
 * @desciption DataRow
 */
public class DataRow extends HashMap<String, Object> {
    public DataRow() {
        super();
    }

    public DataRow(int initialCapacity) {
        super(initialCapacity);
    }
}
