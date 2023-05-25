package com.wbs.common.database.base;

import cn.hutool.core.util.ObjectUtil;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author WBS
 * @date 2023/3/3 8:58
 * @desciption DataTable
 */
public class DataTable implements Serializable {
    private String name;
    private List<DataRow> rows;
    private List<DataColumn> columns;

    public DataTable() {
        this.rows = new CopyOnWriteArrayList<>();
        this.columns = new CopyOnWriteArrayList<>();
    }

    public DataTable(DataTable dt) {
        this.name = dt.getName();
        this.rows = new CopyOnWriteArrayList<>();
        this.columns = ObjectUtil.clone(dt.getColumns());
    }

    public DataTable(String name) {
        this.name = name;
        this.rows = new CopyOnWriteArrayList<>();
        this.columns = new CopyOnWriteArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataRow> getRows() {
        return rows;
    }

    public void setRows(List<DataRow> list) {
        this.rows = ObjectUtil.clone(list);
    }

    public List<DataColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DataColumn> list) {
        this.columns = ObjectUtil.clone(list);
    }

    /**
     * 添加表
     *
     * @param dt
     */
    public DataTable addTable(DataTable dt) {
        this.columns.addAll(ObjectUtil.clone(dt.getColumns()));
        this.rows.addAll(ObjectUtil.clone(dt.getRows()));
        return this;
    }

    /**
     * 添加行
     *
     * @param list
     */
    public void addRows(List<DataRow> list) {
        this.rows.addAll(ObjectUtil.clone(list));
    }

    /**
     * 添加列
     *
     * @param list
     */
    public void addColumns(List<DataColumn> list) {
        for (DataColumn column : list) {
            if (this.columns.stream().noneMatch(x -> x.getName().equals(column.getName()))) {
                this.columns.add(column);
            }
        }
    }

    /**
     * 添加行
     *
     * @param row
     */
    public void addRow(DataRow row) {
        this.rows.add(row);
    }

    /**
     * 添加列
     *
     * @param column
     */
    public void addColumn(DataColumn column) {
        if (this.columns.stream().noneMatch(x -> x.getName().equals(name))) {
            this.columns.add(column);
        }
    }

    /**
     * 根据行
     *
     * @param index
     */
    public void deleteRow(int index) {
        this.rows.remove(index);
    }

    /**
     * 删除行
     *
     * @param row
     */
    public void deleteRow(DataRow row) {
        this.rows.remove(row);
    }

    /**
     * 删除表
     *
     * @param dt
     */
    public DataTable deleteTable(DataTable dt) {
        DataTable newTable = new DataTable(name);
        newTable.columns.addAll(ObjectUtil.clone(this.columns));
        newTable.rows.addAll(ObjectUtil.clone(this.rows));
        newTable.rows.removeAll(dt.getRows());
        return newTable;
    }

    /**
     * 删除列
     *
     * @param index
     */
    public void deleteColumn(int index) {
        this.columns.remove(index);
    }

    /**
     * 更新行
     *
     * @param index
     * @param row
     */
    public void updateRow(int index, DataRow row) {
        this.rows.set(index, row);
    }

    /**
     * 更新列
     *
     * @param index
     * @param column
     */
    public void updateColumn(int index, DataColumn column) {
        this.columns.set(index, column);
    }

    /**
     * 获取行
     *
     * @param index
     * @return
     */
    public DataRow getRow(int index) {
        return this.rows.get(index);
    }

    /**
     * 获取列
     *
     * @param index
     * @return
     */
    public DataColumn getColumn(int index) {
        return this.columns.get(index);
    }

    /**
     * 获取值
     *
     * @param rowIndex
     * @param columnName
     * @return
     */
    public Object getValue(int rowIndex, String columnName) {
        return this.rows.get(rowIndex).getValue(columnName);
    }

    /**
     * 设置值
     *
     * @param rowIndex
     * @param columnName
     * @param value
     */
    public void setValue(int rowIndex, String columnName, Object value) {
        this.rows.get(rowIndex).setValue(columnName, value);
    }

    /**
     * 获取大小
     *
     * @return
     */
    public Integer size() {
        return this.rows.size();
    }

    /**
     * 拷贝
     *
     * @return
     */
    public DataTable copy() {
        return copy(this.name);
    }

    /**
     * 拷贝
     *
     * @return
     */
    public DataTable copy(String newName) {
        DataTable newTable = new DataTable(newName);
        newTable.columns.addAll(ObjectUtil.clone(this.columns));
        newTable.rows.addAll(ObjectUtil.clone(this.rows));
        return newTable;
    }

    public void clear() {
        this.rows.clear();
    }

    /**
     * 排序
     *
     * @param columnName
     * @param asc
     */
    public void sort(String columnName, boolean asc) {
        Comparator<DataRow> comparator = (x, y) -> {
            Object v1 = x.getValue(columnName);
            Object v2 = y.getValue(columnName);
            int result = v1.toString().compareTo(v2.toString());
            return asc ? result : -result;
        };
        this.rows.sort(comparator);
    }
}
