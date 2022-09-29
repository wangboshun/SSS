package com.zny.common.utils;

/**
 * @author WBS
 * Date:2022/9/29
 */

public class PageUtils {

    public static Integer getPageSize(Integer pageSize){
        if (pageSize == null) {
            pageSize = 10;
        }
        return pageSize;
    }

    public static Integer getPageIndex(Integer pageIndex){
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        return pageIndex;
    }
}
