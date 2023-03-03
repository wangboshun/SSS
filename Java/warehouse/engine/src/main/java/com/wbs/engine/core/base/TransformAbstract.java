package com.wbs.engine.core.base;

import com.wbs.engine.model.DataRow;
import com.wbs.engine.model.DataTable;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author WBS
 * @date 2023/3/3 15:59
 * @desciption TransformAbstract
 */
@Component
public class TransformAbstract implements ITransform {
    @Override
    public DataTable mapper(DataTable dt, Map<String, String> config) {
        DataTable newDt = new DataTable();
        for (DataRow item : dt) {
            DataRow row = new DataRow();
            for (Map.Entry<String, String> entry : config.entrySet()) {
                String old = entry.getKey();// 旧键
                if (item.containsKey(old)) {
                    row.put(entry.getValue(), item.get(old));// 设置新的键值对
                }
            }
            newDt.add(row);
        }
        return newDt;
    }
}
