package com.coffee.roastnbrew.exceptions;

import javax.ws.rs.core.Response;

public class UnauthorizedRequest extends APIException {
    
    public UnauthorizedRequest() {
        super(Response.Status.UNAUTHORIZED.getStatusCode(), "requested resource not found");
    }
    
    public UnauthorizedRequest(String message) {
        super(Response.Status.UNAUTHORIZED.getStatusCode(), message);
    }
    
}
