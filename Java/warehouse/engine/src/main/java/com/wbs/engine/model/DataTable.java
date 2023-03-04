package com.wbs.engine.model;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/3 8:58
 * @desciption DataTable
 */
public class DataTable extends ArrayList<DataRow> {

    public DataTable() {

    }

    public DataTable(String name) {
        this.setName(name);
    }

    public DataTable mapper(Map<String, String> config) {
        DataTable result = new DataTable();
        this.forEach(item -> {
            result.add(item.mapper(config));
        });
        return result;
    }

    /**
     * 根据key递增数据
     *
     * @param key   key
     * @param value 传递int、float、double、BIgDecimal数值
     */
    public void increase(String key, Object value) {
        this.forEach(item -> {
            item.increase(key, value);
        });
    }

    /**
     * 根据key递减数据
     *
     * @param key   key
     * @param value 传递int、float、double、BIgDecimal数值
     */
    public void decrease(String key, Object value) {
        this.forEach(item -> {
            item.decrease(key, value);
        });
    }

    /**
     * 插入头部
     *
     * @param key
     * @param value
     */
    public void appendHead(String key, Object value) {
        this.forEach(item -> {
            item.appendHead(key, value);
        });
    }

    /**
     * 插入尾部
     *
     * @param key
     * @param value
     */
    public void appendTail(String key, Object value) {
        this.forEach(item -> {
            item.appendTail(key, value);
        });
    }

    /**
     * 插入指定位置
     *
     * @param key      key
     * @param value    插入值
     * @param position 插入位置，0和1为在头部插入，-1和超出下标在尾部插入
     */
    public void append(String key, Object value, int position) {
        this.forEach(item -> {
            item.append(key, value, position);
        });
    }

    /**
     * 替换对应key内部值
     *
     * @param key  key
     * @param old  odl
     * @param news news
     */
    public void replaceValue(String key, Object old, Object news) {
        this.forEach(item -> {
            item.replaceValue(key, old, news);
        });
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
