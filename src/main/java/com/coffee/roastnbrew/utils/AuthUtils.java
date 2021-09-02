package com.coffee.roastnbrew.utils;

import com.coffee.roastnbrew.constants.Constants;

public class AuthUtils {
    
    public static final String ROLE_USER = "USER";
    public static final String ROLE_HEVO_API_PROXY = "HEVO_API_PROXY";
    
    private AuthUtils() {}
    
    public static boolean isHevoEmail(String email) {
        return email != null && email.endsWith(Constants.HEVO_EMAIL_AT_SUFFIX);
    }
}
