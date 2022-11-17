package com.zny.pipe.component.transform;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zny.common.utils.DataUtils;
import com.zny.pipe.model.ColumnConfigModel;
import com.zny.pipe.model.ConvertConfigModel;
import com.zny.pipe.model.FilterConfigModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WBS
 * Date:2022/11/17
 * 转换帮助类
 */

public class TransformUtils {

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
            String column = item.getKey();
            Object value = item.getValue();
            List<FilterConfigModel> list = filterConfig.stream().filter(x -> x.getFilter_column().equals(column)).collect(Collectors.toList());
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

    /**
     * 数据转换
     *
     * @param data          数值
     * @param convertConfig 转换配置
     */
    public static Map<String, Object> convertData(Map<String, Object> data, List<ConvertConfigModel> convertConfig) {
        for (Map.Entry<String, Object> item : data.entrySet()) {
            String column = item.getKey();
            Object value = item.getValue();
            List<ConvertConfigModel> list = convertConfig.stream().filter(x -> x.getConvert_column().equals(column)).collect(Collectors.toList());
            for (ConvertConfigModel model : list) {
                Object convertValue = model.getConvert_value();
                Object convertNumber = model.getConvert_number();
                String convertSymbol = model.getConvert_symbol();

                //如果判断值为空，则所有值都进行更改
                if (ObjectUtils.isEmpty(convertValue)) {
                    data.put(column, DataUtils.operate(value, convertNumber, convertSymbol));
                }
                //给指定值进行数据转换
                else {
                    FilterConfigModel filterModel = new FilterConfigModel();
                    filterModel.setFilter_symbol("==");
                    filterModel.setFilter_column(column);
                    filterModel.setFilter_type("AND");
                    filterModel.setFilter_value(convertValue.toString());
                    List<FilterConfigModel> filterConfig = new ArrayList<>();
                    filterConfig.add(filterModel);

                    if (haveData(data, filterConfig)) {
                        data.put(column, DataUtils.operate(convertValue, convertNumber, convertSymbol));
                    }
                }
            }
        }
        return data;
    }

    /**
     * 字段映射
     *
     * @param data         数值
     * @param columnConfig 字段配置
     */
    public static Map<String, Object> mapperData(Map<String, Object> data, List<ColumnConfigModel> columnConfig) {
        Map<String, Object> result = new HashMap<>(columnConfig.size());
        for (ColumnConfigModel model : columnConfig) {
            String sinkColumn = model.getSink_column();
            String sourceColumn = model.getSource_column();
            if (data.containsKey(sourceColumn)) {
                Object value = data.get(sourceColumn);
                result.put(sinkColumn, value);
            }
        }
        return result;
    }
}
