package com.zny.pipe.component.transform;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zny.common.utils.DataUtils;
import com.zny.pipe.model.ConvertConfigModel;

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
                    String filterSymbol = model.getFilter_symbol();
                    Object afterValue = model.getConvert_after();
                    Object beforeValue = model.getConvert_before();
                    String convertSymbol = model.getConvert_symbol();

                    //如果判断值为空，则所有值都进行更改
                    if (ObjectUtils.isEmpty(afterValue)) {
                        temp.put(field, DataUtils.operate(value, beforeValue, convertSymbol));
                    } else {
                        //如果没有过滤条件，默认给等于符合
                        if (!StringUtils.isNotBlank(filterSymbol)) {
                            filterSymbol = "==";
                        }
                        if (DataUtils.compare(value, afterValue, filterSymbol)) {
                            temp.put(field, DataUtils.operate(value, beforeValue, convertSymbol));
                        }
                    }
                }
            }
            result.add(temp);
        }
        return result;
    }
}
