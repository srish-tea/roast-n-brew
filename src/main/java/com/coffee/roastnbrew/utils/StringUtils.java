package com.coffee.roastnbrew.utils;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    
    public static boolean isNullOrEmpty(Object str) {
        return str == null || str.toString().trim().isEmpty();
    }
}
