package com.doer.mraims.loanprocess.core.utils;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

public class GsonDateTimeUtil implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern(Constants.DATE_FORMAT);

    @Override
    public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateFormat.print(src));
    }

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.getAsString() == null || json.getAsString().isEmpty()) {
            return null;
        }
        return dateFormat.parseDateTime(json.getAsString());
    }

}
