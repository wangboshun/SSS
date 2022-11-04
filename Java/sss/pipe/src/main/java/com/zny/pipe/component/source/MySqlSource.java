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
 * Date:2022/10/12
 * mysql源端服务类
 */

@Component
@SourceTypeEnum(DbTypeEnum.MySQL)
public class MySqlSource extends SourceAbstract {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 开始
     */
    @Override
    public void start() {
        try {
            this.sourceStatus = TaskStatusEnum.RUNNING;
            this.checkConnection();
            getData();
        } catch (Exception e) {
            logger.error("MySqlSource start", e);
            System.out.println("MySqlSource start: " + e.getMessage());
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
                if (list.size() >= 100) {
                    sendData(list);
                    list.clear();
                }
            }
            if (!list.isEmpty()) {
                sendData(list);
                list.clear();
            }
        } catch (SQLException e) {
            DbEx.release(connection, pstm);
            logger.error("MySqlSource getData", e);
            System.out.println("MySqlSource getData: " + e.getMessage());
        } finally {
            DbEx.release(pstm);
        }
        this.stop();
    }
}
