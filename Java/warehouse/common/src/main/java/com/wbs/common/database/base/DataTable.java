package com.wbs.common.database.base;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author WBS
 * @date 2023/3/3 8:58
 * @desciption DataTable
 */
public class DataTable extends ArrayList<DataRow> {

    private String name;

    public DataTable() {

    }

    public DataTable(String name) {
        this.setName(name);
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

    /**
     * 拆成N份
     *
     * @param arraySize
     * @return
     */
    public List<DataTable> splitArray(int arraySize) {
        List<DataTable> result = new ArrayList<>();
        int batchSize = (int) Math.ceil((double) this.size() / arraySize);
        List<List<DataRow>> list = IntStream.range(0, arraySize).parallel().mapToObj(i -> this.subList(i * batchSize, Math.min((i + 1) * batchSize, this.size()))).collect(Collectors.toList());
        for (List<DataRow> rows : list) {
            DataTable dt = new DataTable();
            dt.addAll(rows);
            result.add(dt);
        }
        return result;
    }

    /**
     * 按批次拆分
     *
     * @param size
     * @return
     */
    public List<DataTable> splitBatch(int size) {
        List<DataTable> result = new ArrayList<>();
        List<List<DataRow>> list = Lists.partition(this, size);
        for (List<DataRow> rows : list) {
            DataTable dt = new DataTable();
            dt.addAll(rows);
            result.add(dt);
        }
        return result;
    }

    /**
     * datatable分页
     *
     * @param dt       dt
     * @param pageNum  页码
     * @param pageSize 页大小
     */
    public static List<DataTable> getPage(List<DataTable> dt, int pageNum, int pageSize) {
        int startIndex = pageNum * pageSize;
        int endIndex = startIndex + pageSize;
        if (startIndex > dt.size()) {
            startIndex = dt.size();
        }
        if (endIndex > dt.size()) {
            endIndex = dt.size();
        }
        return dt.subList(startIndex, endIndex);
    }
}
