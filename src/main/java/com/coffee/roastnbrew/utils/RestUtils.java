package com.coffee.roastnbrew.utils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

public class RestUtils {

    public static <T> Response ok(T data) {
        return ok(data, null);
    }

    public static <T> Response ok(T data, NewCookie cookie) {

        ApiResponse<T> successResponse = ApiResponse.successResponse(data);

        Response.ResponseBuilder builder = Response.ok(successResponse, MediaType.APPLICATION_JSON_TYPE);
        if (cookie != null) {
            builder.cookie(cookie);
        }
        return builder.build();
    }

    public static Response noContentResponse() {
        return Response.noContent().build();
    }
}
