package com.wbs.engine.model;

import com.wbs.common.database.base.DataTable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/3/11 15:05
 * @desciption WriterResult
 */
@Setter
@Getter
public class WriterResult implements Serializable {
    private String spend;
    private DataTable errorData;
    private DataTable existData;
    private int insertCount;
    private int updateCount;
    private int errorCount;
    private int exitsCount;
    private int ignoreCount;
}
