package com.doer.mraims.loanprocess.core.utils;

import com.nimbusds.jose.shaded.gson.Gson;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UtilService {

    private static Gson gson;

    public UtilService(Gson gson) {
        UtilService.gson = gson;
    }

    public static Map<String, Object> toCamelCaseKeys(Map<String, Object> originalMap) {
        Map<String, Object> camelCaseMap = new HashMap<>();
        originalMap.forEach((key, value) -> {
            String camelCaseKey = toCamelCase(key);
            camelCaseMap.put(camelCaseKey, value);
        });
        return camelCaseMap;
    }

    private static String toCamelCase(String snakeCase) {
        StringBuilder result = new StringBuilder();
        boolean upperCaseNext = false;

        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                upperCaseNext = true;
            } else if (upperCaseNext) {
                result.append(Character.toUpperCase(c));
                upperCaseNext = false;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static Date convertToDate(Object dateObj) {
        if (dateObj instanceof java.sql.Timestamp) {
            return new Date(((java.sql.Timestamp) dateObj).getTime());
        } else if (dateObj instanceof String) {
            return Date.valueOf(dateObj.toString());
        } else {
            throw new IllegalArgumentException("Unexpected data type for date: " + (dateObj != null ? dateObj.getClass() : "null"));
        }
    }

    public static <T> List<T> convertJSONArrayToList(JSONArray jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            T obj = gson.fromJson(jsonArray.getJSONObject(i).toString(), clazz);
            list.add(obj);
        }
        return list;
    }

    public static Boolean checkNullOrEmpty(final String s) {
        return s == null || s.trim().isEmpty();
    }

}
