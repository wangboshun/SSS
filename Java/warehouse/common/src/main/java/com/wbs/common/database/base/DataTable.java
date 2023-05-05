package com.wbs.common.database.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * @date 2023/3/3 8:58
 * @desciption DataTable
 */
public class DataTable extends ArrayList<DataRow> implements Cloneable {

    private String name;

    public DataTable() {

    }

    @Override
    public DataTable clone() {
        return (DataTable) super.clone();
    }

    public DataTable(String name) {
        this.setName(name);
    }

    public DataTable(List<DataRow> rows) {
        if(rows!=null&&rows.size() > 0){
            rows.forEach(this::addRow);
        }
    }

    /**
     * 删除key
     *
     * @param keys
     */
    public void removeKeys(List<String> keys) {
        this.forEach(item -> item.removeKeys(keys));
    }

    /**
     * 添加一行数据
     *
     * @param row
     */
    public void addRow(DataRow row) {
        this.forEach(item -> item.putAll(row));
    }

    public DataTable addDt(DataTable dt) {
        if(dt!=null){
            this.addAll(dt);
        }
        return this;
    }

    /**
     * 数据映射
     *
     * @param config
     * @return
     */
    public DataTable mapper(Map<String, String> config) {
        DataTable result = new DataTable();
        this.forEach(item -> result.add(item.mapper(config)));
        return result;
    }

    /**
     * 根据key递增数据
     *
     * @param key   key
     * @param value 传递int、float、double、BIgDecimal数值
     */
    public void increase(String key, Object value) {
        this.forEach(item -> item.increase(key, value));
    }

    /**
     * 根据key递减数据
     *
     * @param key   key
     * @param value 传递int、float、double、BIgDecimal数值
     */
    public void decrease(String key, Object value) {
        this.forEach(item -> item.decrease(key, value));
    }

    /**
     * 插入头部
     *
     * @param key
     */
    public void appendHead(String key) {
        this.forEach(item -> item.appendHead(key));
    }

    /**
     * 插入尾部
     *
     * @param key
     */
    public void appendTail(String key) {
        this.forEach(item -> item.appendTail(key));
    }

    /**
     * 插入指定位置
     *
     * @param key      key
     * @param value    插入值
     * @param position 插入位置，0和1为在头部插入，-1和超出下标在尾部插入
     */
    public void append(String key, Object value, int position) {
        this.forEach(item -> item.append(key, value, position));
    }

    /**
     * 替换对应key内部值
     *
     * @param key  key
     * @param old  odl
     * @param news news
     */
    public void replaceValue(String key, Object old, Object news) {
        this.forEach(item -> item.replaceValue(key, old, news));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
