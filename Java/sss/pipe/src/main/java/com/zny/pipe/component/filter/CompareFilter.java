package com.zny.pipe.component.filter;

import com.zny.pipe.model.FilterConfigModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date 2022-11-11 16:12
 * CompareFilter 数值对比过滤器
 */

public class CompareFilter implements FilterBase {

    private List<FilterConfigModel> filterConfig;

    public void config(List<FilterConfigModel> config) {
        this.filterConfig = config;
    }

    /**
     * 数值筛选
     *
     * @param data 数据集
     */
    @Override
    public List<Map<String, Object>> filter(List<Map<String, Object>> data) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : data) {
            if (FilterUtils.haveData(map, filterConfig)) {
                result.add(map);
            }
        }
        return result;
    }


}
