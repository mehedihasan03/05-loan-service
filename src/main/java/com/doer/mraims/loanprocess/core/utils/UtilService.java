package com.doer.mraims.loanprocess.core.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UtilService {

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

}
