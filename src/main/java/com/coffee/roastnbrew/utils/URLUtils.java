package com.coffee.roastnbrew.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils {
    
    public static String cleanNextUrl(String next) {
        next = next == null ? "/" : next;
        if (!next.startsWith("/")) {
            try {
                next = new URL(next).getFile();
            } catch (MalformedURLException e) {
                next = "/";
            }
        }
        return next;
    }
}
