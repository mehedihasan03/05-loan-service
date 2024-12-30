package com.doer.mraims.loanprocess.core.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UtilService {

    public <T> Map<String, Map<String, T>> featureQueryMap(Class<T> queryProviderClass, Map<String, T> queryProviders) {

        Map<String, Map<String, T>> featureQueryMap = new HashMap<>();

        queryProviders.forEach((beanName, queryProvider) -> {
            String[] parts = beanName.split("(?=[A-Z])");
            if (parts.length < 2) {
                throw new IllegalStateException("Invalid QueryProvider name: " + beanName);
            }

            String featureName = parts[0].toLowerCase();
            String dbType = parts[1].toLowerCase();

            featureQueryMap
                    .computeIfAbsent(featureName, key -> new HashMap<>())
                    .put(dbType, queryProvider);
        });

        return featureQueryMap;
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

}
