package com.zny.pipe.component.source;

import com.zny.common.enums.DbTypeEnum;
import com.zny.common.utils.DbEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
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
public class MsSqlSource extends SourceAbstract implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 结束
     */
    @Override
    public void start() {
        try {
            getData();
        } catch (Exception e) {
            logger.error("MsSql start", e);
            System.out.println("MsSql start: " + e.getMessage());
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        try {
            String sql = getNextSql();
            PreparedStatement stmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            ResultSet result = stmt.executeQuery();
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
            if (list.size() > 0) {
                sendData(list);
            }
        } catch (SQLException e) {
            logger.error("MsSql getData", e);
            System.out.println("MsSql getData: " + e.getMessage());
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("MsSql getData", e);
                System.out.println("MsSql getData: " + e.getMessage());
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SourceFactory.register(DbTypeEnum.MsSQL, this);
    }
}
