package com.doer.mraims.loanprocess.core.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonToDtoMapper {

    public static <T> T mapJsonToDto(JSONObject jsonObject, Class<T> dtoClass) {
        try {
            T dto = dtoClass.getDeclaredConstructor().newInstance();

            for (Field field : dtoClass.getDeclaredFields()) {
                field.setAccessible(true);

                String fieldName = field.getName();
                if (jsonObject.has(fieldName)) {
                    Object value = jsonObject.get(fieldName);

                    if (field.getType().equals(List.class)) {
                        if (value instanceof org.json.JSONArray jsonArray) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(jsonArray.getString(i));
                            }
                            field.set(dto, list);
                        }
                    } else if (field.getType().equals(LocalDate.class)) {
                        if (value instanceof String dateValue) {
                            field.set(dto, LocalDate.parse(dateValue));
                        }
                    } else {
                        field.set(dto, value);
                    }
                }
            }

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map JSONObject to DTO: " + dtoClass.getSimpleName(), e);
        }
    }

    public static <T> List<T> mapToDtoList(JSONArray jsonArray, Class<T> dtoClass) {
        List<T> dtoList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T dto = mapJsonToDto(jsonObject, dtoClass);
            dtoList.add(dto);
        }
        return dtoList;
    }
}

