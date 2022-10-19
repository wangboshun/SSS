package com.zny.pipe.component.source;

import com.zny.common.utils.DbEx;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class MySqlSource extends SourceAbstract {

    /**
     * 开始
     */
    @Override
    public void start() {
        try {
            getData();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        try {
            Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MAX_VALUE);
            ResultSet result = stmt.executeQuery(getNextSql());
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
            if (list.size() > 0) {
                sendData(list);
                list.clear();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {

            }
        }
    }
}
