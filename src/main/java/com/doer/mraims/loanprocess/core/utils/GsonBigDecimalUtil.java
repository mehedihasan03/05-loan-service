package com.doer.mraims.loanprocess.core.utils;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class GsonBigDecimalUtil implements JsonSerializer<BigDecimal>, JsonDeserializer<BigDecimal> {
	
	@Override
	public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
		if (src == null) {
			return null;
		}
		return new JsonPrimitive(src.toPlainString());
		
	}

	@Override
	public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (StringUtils.isBlank(json.getAsString())) {
			return null;
		}
        return new BigDecimal(json.getAsString().trim());
	}
	
}
