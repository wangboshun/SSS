package com.zny.common.model;

import java.io.Serializable;

/**
 * @author WBS
 * Date:2022/9/28
 */

public class PageResult implements Serializable {
    public long total;
    public Object rows;
    public long pages;
    public long current;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }
}
