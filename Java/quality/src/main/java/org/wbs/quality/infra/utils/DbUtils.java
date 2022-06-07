package org.wbs.quality.infra.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author WBS
 * Date:2022/6/1
 */

public class DbUtils {
    public static Object getData(ResultSet result, String column) {
        Object obj;
        try {
            obj = result.getObject(column);
            if ("java.time.LocalDateTime".equals(obj.getClass().getName())) {
                return obj.toString();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }
}
