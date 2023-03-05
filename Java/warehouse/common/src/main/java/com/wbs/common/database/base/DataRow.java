package com.wbs.common.database.base;

import com.wbs.common.utils.DataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/3 8:57
 * @desciption DataRow
 */
public class DataRow extends LinkedHashMap<String, Object> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DataRow() {
        super();
    }

    public DataRow(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * 删除key
     *
     * @param keys
     */
    public void removeKeys(List<String> keys) {
        keys.forEach(this::remove);
    }

    /**
     * 数据映射
     *
     * @param config
     * @return
     */
    public DataRow mapper(Map<String, String> config) {
        DataRow row = new DataRow();
        config.forEach((old, news) -> {
            if (config.containsKey(old)) {
                row.put(news, this.get(old));
            }
        });
        return row;
    }

    /**
     * 根据key递增数据
     *
     * @param key   key
     * @param value 传递int、float、double、BIgDecimal数值
     */
    public void increase(String key, Object value) {
        Object v = this.get(key);
        if (!DataUtils.isNumber(v) || !DataUtils.isNumber(value)) {
            return;
        }
        String type = v.getClass().getSimpleName().toLowerCase();
        try {
            if (type.contains("int")) {
                int param = DataUtils.toInt(value);
                int current = DataUtils.toInt(v);
                this.replace(key, param + current);
            } else if ("float".equals(type)) {
                float param = DataUtils.toFloat(value);
                float current = DataUtils.toFloat(v);
                this.replace(key, param + current);
            } else if ("double".equals(type)) {
                double param = DataUtils.toDouble(value);
                double current = DataUtils.toDouble(v);
                this.replace(key, param + current);
            } else if ("bigdecimal".equals(type)) {
                BigDecimal param = DataUtils.toDecimal(value);
                BigDecimal current = DataUtils.toDecimal(v);
                this.replace(key, param.add(current));
            }
        } catch (Exception e) {
            logger.error("------DataRow increase error------", e);
        }
    }

    /**
     * 根据key递减数据
     *
     * @param key   key
     * @param value 传递int、float、double、BIgDecimal数值
     */
    public void decrease(String key, Object value) {
        Object v = this.get(key);
        if (!DataUtils.isNumber(v) || !DataUtils.isNumber(value)) {
            return;
        }
        String type = v.getClass().getSimpleName().toLowerCase();
        try {
            if (type.contains("int")) {
                int param = DataUtils.toInt(value);
                int current = DataUtils.toInt(v);
                this.replace(key, param - current);
            } else if ("float".equals(type)) {
                float param = DataUtils.toFloat(value);
                float current = DataUtils.toFloat(v);
                this.replace(key, param - current);
            } else if ("double".equals(type)) {
                double param = DataUtils.toDouble(value);
                double current = DataUtils.toDouble(v);
                this.replace(key, param - current);
            } else if ("bigdecimal".equals(type)) {
                BigDecimal param = DataUtils.toDecimal(value);
                BigDecimal current = DataUtils.toDecimal(v);
                this.replace(key, param.subtract(current));
            }
        } catch (Exception e) {
            logger.error("------DataRow increase error------", e);
        }
    }

    /**
     * 插入头部
     *
     * @param key
     * @param value
     */
    public void appendHead(String key, Object value) {
        this.append(key, values(), 0);
    }

    /**
     * 插入尾部
     *
     * @param key
     * @param value
     */
    public void appendTail(String key, Object value) {
        this.append(key, values(), -1);
    }

    /**
     * 插入指定位置
     *
     * @param key      key
     * @param value    插入值
     * @param position 插入位置，0和1为在头部插入，-1和超出下标在尾部插入
     */
    public void append(String key, Object value, int position) {
        try {
            Object v = this.get(key);
            String current = v.toString();
            String param = value.toString();
            String result = "";
            if (position == 0 || position == 1) { // 插入头部
                result = param + current;
            } else if (position < 0) {// 插入尾部
                result = current + param;
            } else {
                position -= 1;
                // 如果位置超出字符串，变为在尾部插入
                if (position >= current.length()) {
                    result = current + param;
                } else {
                    String start = current.substring(0, position);
                    String end = current.substring(position, current.length() - 1);
                    result = start + param + end;
                }
            }
            String type = v.getClass().getSimpleName().toLowerCase();
            // 如果是数字，需要判断是否合规
            if (type.contains("int") || "float".equals(type) || "double".equals(type) || "bigdecimal".equals(type)) {
                if (!DataUtils.isNumber(result)) {
                    return;
                }
            }

            if (type.contains("int")) {
                this.replace(key, DataUtils.toInt(result));
            } else if ("float".equals(type)) {
                this.replace(key, DataUtils.toFloat(result));
            } else if ("double".equals(type)) {
                this.replace(key, DataUtils.toDouble(result));
            } else if ("bigdecimal".equals(type)) {
                this.replace(key, DataUtils.toDecimal(result));
            } else if ("string".equals(type)) {
                this.replace(key, result);
            }
        } catch (Exception e) {
            logger.error("------DataRow append error------", e);
        }
    }

    /**
     * 替换对应key内部值
     *
     * @param key  key
     * @param old  odl
     * @param news news
     */
    public void replaceValue(String key, Object old, Object news) {
        Object v = this.get(key);
        String current = v.toString();
        String result = current.replaceAll(old.toString(), news.toString());
        String type = v.getClass().getSimpleName().toLowerCase();
        // 如果是数字，需要判断是否合规
        if (type.contains("int") || "float".equals(type) || "double".equals(type) || "bigdecimal".equals(type)) {
            if (!DataUtils.isNumber(result)) {
                return;
            }
        }
        if (type.contains("int")) {
            this.replace(key, DataUtils.toInt(result));
        } else if ("float".equals(type)) {
            this.replace(key, DataUtils.toFloat(result));
        } else if ("double".equals(type)) {
            this.replace(key, DataUtils.toDouble(result));
        } else if ("bigdecimal".equals(type)) {
            this.replace(key, DataUtils.toDecimal(result));
        } else if ("string".equals(type)) {
            this.replace(key, result);
        }
    }
}
