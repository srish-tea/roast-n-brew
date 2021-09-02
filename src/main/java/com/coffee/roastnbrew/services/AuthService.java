package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface AuthService {
    
    String getAuthorizationUrl(String next, String code, String errorPath) throws JsonProcessingException;
    
    LoginResponse createOrUpdateUser(String code) throws CoffeeException;
}
