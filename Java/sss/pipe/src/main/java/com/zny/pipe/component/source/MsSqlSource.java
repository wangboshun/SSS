package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;
import com.zny.common.utils.DbEx;
import com.zny.pipe.component.base.SourceAbstract;
import com.zny.pipe.component.enums.SourceTypeEnum;
import com.zny.pipe.component.enums.TaskStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/10/14
 * mssql源端服务类
 */

@Component
@SourceTypeEnum(DbTypeEnum.MsSQL)
public class MsSqlSource extends SourceAbstract {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 结束
     */
    @Override
    public void start() {
        try {
            this.sourceStatus = TaskStatusEnum.Running;
            this.checkConnection();
            getData();
        } catch (Exception e) {
            logger.error("MsSqlSource start", e);
            System.out.println("MsSqlSource start: " + e.getMessage());
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        PreparedStatement pstm = null;
        try {
            String sql = getNextSql();
            pstm = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            pstm.setFetchSize(Integer.MIN_VALUE);
            ResultSet result = pstm.executeQuery();
            List<Map<String, Object>> list = new ArrayList<>();
            List<String> filedList = DbEx.getField(result);
            while (result.next()) {
                Map<String, Object> rowData = new HashMap<>(filedList.size());
                for (String x : filedList) {
                    rowData.put(x, result.getObject(x));
                }
                list.add(rowData);
                if (list.size() >= 10) {
                    List<Map<String, Object>> msg = new ArrayList<>(list);
                    list.clear();
                    sendData(msg);
                }
            }
            if (!list.isEmpty()) {
                sendData(list);
            }
        } catch (SQLException e) {
            release(connection, pstm);
            logger.error("MsSqlSource getData", e);
            System.out.println("MsSqlSource getData: " + e.getMessage());
        } finally {
            release(pstm);
        }
        this.stop();
    }
}
