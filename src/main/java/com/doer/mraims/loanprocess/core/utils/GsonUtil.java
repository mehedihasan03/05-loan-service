package com.doer.mraims.loanprocess.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Synchronized;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component("gsonUtil")
public class GsonUtil {
	private Gson gson = new GsonBuilder()
			.registerTypeAdapter(BigDecimal.class, new GsonBigDecimalUtil())
			.registerTypeAdapter(DateTime.class, new GsonDateTimeUtil()).create();

		@Synchronized
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Object parseObject(String json, Class clazz) {
			return gson.fromJson(json, clazz);
		}

		@Synchronized
		public String getJson(Object obj) {
			GsonBuilder gson = new GsonBuilder()
				.registerTypeAdapter(BigDecimal.class, new GsonBigDecimalUtil())
				.registerTypeAdapter(DateTime.class, new GsonDateTimeUtil());
			if(Constants.IS_PREETY_JSON){
				gson.setPrettyPrinting();
			}
			return gson.create().toJson(obj);
		}

		@Synchronized
		public <T> Map<String, List<T>> parseData(String json, Type type) {
			Map<String, List<T>> obj = gson.fromJson(json, type);
			return obj;
		}

		@Synchronized
	    public <T> T  parseToObj(String json, Class<T> t) {
	    	T obj = gson.fromJson(json, t);
	        return obj;
	    }
		
		@Synchronized
		public <T> List<T> jsonArrayToList(String json, Class<T> elementClass) {
		    ObjectMapper objectMapper = new ObjectMapper();
		    List<T> objectList = null;
		    try {
		    CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elementClass);
		    objectList = objectMapper.readValue(json, listType);
		    } catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return objectList;
		}

		@Synchronized
	    public <T> List<T>  parseToList(String json, Class<T> t) {
			ObjectMapper mapper = new ObjectMapper();
			List<T> objList = null;
			try {
				objList = mapper.readValue(json, new TypeReference<List<T>>(){});
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return objList;
	    }

	@Synchronized
	public Object getJsonWithExposedFields(JSONObject jsonObject) {
		GsonBuilder builder = new GsonBuilder()
				.registerTypeAdapter(BigDecimal.class, new GsonBigDecimalUtil())
				.registerTypeAdapter(DateTime.class, new GsonDateTimeUtil());
		builder.excludeFieldsWithoutExposeAnnotation();
		builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE, Modifier.FINAL);
		builder.serializeNulls();
		if(Constants.IS_PREETY_JSON){
			builder.setPrettyPrinting();
		}
		return builder.create().toJson(jsonObject);
	}

		@Synchronized
		public String getJsonWithExposedDateFields(Object obj, String dateFormat) {
			String df = Constants.DATE_FORMAT;
			if (!StringUtils.isNotBlank(dateFormat)) {
				df = new String(dateFormat);
			}
			GsonBuilder builder = new GsonBuilder()
				.registerTypeAdapter(BigDecimal.class, new GsonBigDecimalUtil())
				.registerTypeAdapter(DateTime.class, new GsonDateTimeUtil());
			builder.excludeFieldsWithoutExposeAnnotation();
			builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE, Modifier.FINAL);
			builder.serializeNulls();
			builder.setDateFormat(df);
			if(Constants.IS_PREETY_JSON){
				builder.setPrettyPrinting();
			}
			return builder.create().toJson(obj);
		}

}
