package com.zny.pipe.component.transform;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zny.common.utils.DataUtils;
import com.zny.pipe.component.filter.FilterUtils;
import com.zny.pipe.model.ConvertConfigModel;
import com.zny.pipe.model.FilterConfigModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WBS
 * Date 2022-10-28 15:18
 * Transform抽象类
 */

public class TransformAbstract implements TransformBase {

    private List<ConvertConfigModel> transformConfig;

    @Override
    public void config(List<ConvertConfigModel> config) {
        this.transformConfig = config;
    }

    @Override
    public List<Map<String, Object>> convert(List<Map<String, Object>> data) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : data) {
            Map<String, Object> temp = new HashMap<>(map);
            for (Map.Entry<String, Object> item : map.entrySet()) {
                String field = item.getKey();
                Object value = item.getValue();
                List<ConvertConfigModel> list = transformConfig.stream().filter(x -> x.getConvert_field().equals(field)).collect(Collectors.toList());
                for (ConvertConfigModel model : list) {
                    Object convertValue = model.getConvert_value();
                    Object convertNumber = model.getConvert_number();
                    String convertSymbol = model.getConvert_symbol();

                    //如果判断值为空，则所有值都进行更改
                    if (ObjectUtils.isEmpty(convertValue)) {
                        temp.put(field, DataUtils.operate(value, convertNumber, convertSymbol));
                    }
                    //给指定值进行数据转换
                    else {
                        FilterConfigModel filterModel = new FilterConfigModel();
                        filterModel.setFilter_symbol("==");
                        filterModel.setFilter_field(field);
                        filterModel.setFilter_type("AND");
                        filterModel.setFilter_value(convertValue.toString());
                        List<FilterConfigModel> filterConfig = new ArrayList<>();
                        filterConfig.add(filterModel);

                        if (FilterUtils.haveData(map, filterConfig)) {
                            temp.put(field, DataUtils.operate(convertValue, convertNumber, convertSymbol));
                        }
                    }
                }
            }
            result.add(temp);
        }
        return result;
    }
}
