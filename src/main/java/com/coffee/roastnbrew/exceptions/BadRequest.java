package com.coffee.roastnbrew.exceptions;

import javax.ws.rs.core.Response;

public class BadRequest extends APIException {
    
    public BadRequest(String message) {
        super(Response.Status.BAD_REQUEST.getStatusCode(), message);
    }
}
