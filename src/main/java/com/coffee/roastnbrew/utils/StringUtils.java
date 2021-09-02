package com.coffee.roastnbrew.utils;

import java.util.HashMap;
import java.util.Map;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static boolean isNullOrEmpty(Object str) {
        return str == null || str.toString().trim().isEmpty();
    }

    public static Map<String, String> toMap(String value) {
        value = StringUtils.substringBetween(value, "{", "}");
        ;
        String[] keyValuePairs = value.split(",");
        Map<String, String> map = new HashMap<>();

        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            map.put(entry[0].trim(), entry[1].trim());
        }
        return map;
    }
}
