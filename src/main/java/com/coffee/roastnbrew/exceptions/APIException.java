package com.coffee.roastnbrew.exceptions;

import javax.ws.rs.core.Response;
import lombok.Getter;

public class APIException extends CoffeeException {
    
    @Getter
    private Response.Status status;
    
    public APIException(int statusCode, String errorMessage) {
        super(errorMessage);
        this.status = Response.Status.fromStatusCode(statusCode);
    }
    
    public APIException(int statusCode, String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
        this.status = Response.Status.fromStatusCode(statusCode);
    }
    
    public Throwable getOriginalException() {
        return this.getCause();
    }
    
    public static final int HTTP_TOO_MANY_REQUESTS = 429;
}
