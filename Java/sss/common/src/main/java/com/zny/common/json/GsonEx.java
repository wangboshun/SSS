package com.zny.common.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author WBS
 * Date:2022/10/13
 * Gson扩展类
 */

public class GsonEx {

    public static Gson getInstance() {
        return new GsonBuilder().setPrettyPrinting().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).
                registerTypeAdapter(Timestamp.class, new TimestampAdapter()).
                create();
    }

}
