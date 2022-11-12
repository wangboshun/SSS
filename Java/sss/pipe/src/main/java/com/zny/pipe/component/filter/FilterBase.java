package com.zny.pipe.component.filter;

import com.zny.pipe.model.FilterConfigModel;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date 2022-11-11 16:11
 * FilterBase
 */

public interface FilterBase {
    void config(List<FilterConfigModel> config);
    List<Map<String, Object>> filter(List<Map<String, Object>> data);
}
