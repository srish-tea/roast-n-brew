package com.coffee.roastnbrew.utils;

import java.util.List;

public class ListUtils {
    
    public static <T> boolean isEmpty(final List<T> list) {
        return list == null || list.isEmpty();
    }
    
}
