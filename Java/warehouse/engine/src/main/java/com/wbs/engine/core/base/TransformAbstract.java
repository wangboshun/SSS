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
        dt.forEach(item -> {
            DataRow row = new DataRow();
            config.forEach((old, news) -> {
                if (item.containsKey(old)) {
                    row.put(news, item.get(old));
                }
            });
            newDt.add(row);
        });
        return newDt;
    }
}
