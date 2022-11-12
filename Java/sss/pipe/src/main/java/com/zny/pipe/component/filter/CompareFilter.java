package com.zny.pipe.component.filter;

import com.zny.common.utils.DataUtils;
import com.zny.pipe.model.FilterConfigModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            List<Boolean> flag = new ArrayList<>();
            for (Map.Entry<String, Object> item : map.entrySet()) {
                String field = item.getKey();
                Object value = item.getValue();
                List<FilterConfigModel> list = filterConfig.stream().filter(x -> x.getFilter_field().equals(field)).collect(Collectors.toList());
                for (FilterConfigModel model : list) {
                    Object filterValue = model.getFilter_value();
                    String filterSymbol = model.getFilter_symbol();
                    flag.add(DataUtils.compare(value, filterValue, filterSymbol));
                }
            }
            //如果不包含任何的false情况，添加该条数据
            if (!flag.contains(false)) {
                result.add(map);
            }
        }
        return result;
    }
}
