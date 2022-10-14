package com.zny.common.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime序列化
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    /**
     * 序列化
     */
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        String format = "yyyy-MM-dd HH:mm:ss";
        //毫秒
        if (localDateTime.getNano() > 0) {
            format += ":SSS";
        }
        return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern(format)));
    }

    /**
     * 反序列化
     */
    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String tm = jsonElement.getAsJsonPrimitive().getAsString();
        String format = "yyyy-MM-dd HH:mm:ss";
        //毫秒
        if (tm.length() > 20) {
            format += ":SSS";
        }
        return LocalDateTime.parse(tm, DateTimeFormatter.ofPattern(format));
    }
}

