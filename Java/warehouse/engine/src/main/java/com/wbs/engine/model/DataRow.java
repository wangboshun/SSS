package com.wbs.engine.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
        if (!isNumber(v) || !isNumber(value)) {
            return;
        }
        String type = v.getClass().getSimpleName().toLowerCase();
        try {
            if (type.contains("int")) {
                int param = Integer.parseInt(value.toString());
                int current = Integer.parseInt(v.toString());
                this.replace(key, param + current);
            } else if ("float".equals(type)) {
                float param = Float.parseFloat(value.toString());
                float current = Float.parseFloat(v.toString());
                this.replace(key, param + current);
            } else if ("double".equals(type)) {
                double param = Double.parseDouble(value.toString());
                double current = Double.parseDouble(v.toString());
                this.replace(key, param + current);
            } else if ("bigdecimal".equals(type)) {
                BigDecimal param = new BigDecimal(value.toString());
                BigDecimal current = new BigDecimal(v.toString());
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
        if (!isNumber(v) || !isNumber(value)) {
            return;
        }
        String type = v.getClass().getSimpleName().toLowerCase();
        try {
            if (type.contains("int")) {
                int param = Integer.parseInt(value.toString());
                int current = Integer.parseInt(v.toString());
                this.replace(key, param - current);
            } else if ("float".equals(type)) {
                float param = Float.parseFloat(value.toString());
                float current = Float.parseFloat(v.toString());
                this.replace(key, param - current);
            } else if ("double".equals(type)) {
                double param = Double.parseDouble(value.toString());
                double current = Double.parseDouble(v.toString());
                this.replace(key, param - current);
            } else if ("bigdecimal".equals(type)) {
                BigDecimal param = new BigDecimal(value.toString());
                BigDecimal current = new BigDecimal(v.toString());
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
                if (!isNumber(result)) {
                    return;
                }
            }

            if (type.contains("int")) {
                this.replace(key, Integer.parseInt(result));
            } else if ("float".equals(type)) {
                this.replace(key, Float.parseFloat(result));
            } else if ("double".equals(type)) {
                this.replace(key, Double.parseDouble(result));
            } else if ("bigdecimal".equals(type)) {
                this.replace(key, new BigDecimal(result));
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
            if (!isNumber(result)) {
                return;
            }
        }
        if (type.contains("int")) {
            this.replace(key, Integer.parseInt(result));
        } else if ("float".equals(type)) {
            this.replace(key, Float.parseFloat(result));
        } else if ("double".equals(type)) {
            this.replace(key, Double.parseDouble(result));
        } else if ("bigdecimal".equals(type)) {
            this.replace(key, new BigDecimal(result));
        } else if ("string".equals(type)) {
            this.replace(key, result);
        }
    }

    /**
     * 检查是否是数字
     *
     * @param value
     * @return
     */
    private boolean isNumber(Object value) {
        if (value == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(value.toString()).matches();
    }
}
