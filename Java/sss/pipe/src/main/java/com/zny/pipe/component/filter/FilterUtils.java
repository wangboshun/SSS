package com.zny.pipe.component.filter;

import com.zny.common.utils.DataUtils;
import com.zny.pipe.model.FilterConfigModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WBS
 * Date 2022-11-13 17:28
 * FilterUtils
 */

/**
 * 过滤帮助类
 */
public class FilterUtils {

    /**
     * 是否符合过滤条件
     *
     * @param data         数据
     * @param filterConfig 过滤条件配置
     */
    public static boolean haveData(Map<String, Object> data, List<FilterConfigModel> filterConfig) {
        List<Boolean> flagList = new ArrayList<>();
        List<String> fliterTypeList = new ArrayList<>();
        for (Map.Entry<String, Object> item : data.entrySet()) {
            String field = item.getKey();
            Object value = item.getValue();
            List<FilterConfigModel> list = filterConfig.stream().filter(x -> x.getFilter_field().equals(field)).collect(Collectors.toList());
            for (FilterConfigModel model : list) {
                fliterTypeList.add(model.getFilter_type().toUpperCase());
                Object filterValue = model.getFilter_value();
                String filterSymbol = model.getFilter_symbol();
                flagList.add(DataUtils.compare(value, filterValue, filterSymbol));
            }
        }
        //如果是AND条件
        if (fliterTypeList.stream().allMatch(x -> x.equals("AND"))) {
            //如果不包含任何的false情况，添加该条数据
            if (!flagList.contains(false)) {
                return true;
            }
        }
        //如果是OR条件
        else if (fliterTypeList.contains("OR")) {
            //只要满足一个条件，添加该条数据
            if (flagList.contains(true)) {
                return true;
            }
        }
        return false;
    }
}
