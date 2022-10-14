package com.zny.common.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Timestamp序列化
 */
public class TimestampAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

    /**
     * 序列化
     */
    @Override
    public JsonElement serialize(Timestamp timestamp, Type type, JsonSerializationContext jsonSerializationContext) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //毫秒
        if (timestamp.getNanos() > 0) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        }
        return new JsonPrimitive(format.format(new Date(timestamp.getTime())));
    }

    /**
     * 反序列化
     */
    @Override
    public Timestamp deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tm = jsonElement.getAsString();
        //毫秒
        if (tm.length() > 20) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        }
        try {
            return new Timestamp(format.parse(tm).getTime());
        } catch (ParseException e) {

        }
        return null;
    }
}