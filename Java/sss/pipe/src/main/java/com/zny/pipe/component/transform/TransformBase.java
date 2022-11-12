package com.zny.pipe.component.transform;

import com.zny.pipe.model.ConvertConfigModel;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date 2022-10-28 15:18
 * Transform基类
 */

public interface TransformBase {
    void config(List<ConvertConfigModel> config);
    List<Map<String, Object>> convert (List<Map<String, Object>> data);
}
