package com.coffee.roastnbrew.resources;

import com.coffee.roastnbrew.exceptions.BadRequest;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.auth.LoginResponse;
import com.coffee.roastnbrew.services.AuthService;
import com.coffee.roastnbrew.utils.RestUtils;
import com.coffee.roastnbrew.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("/coffee/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authService;
    
    @Inject
    public AuthResource(AuthService authService) {
        this.authService = authService;
    }
    
    @GET
    @Path("/authenticate")
    public Response authenticate(
        @QueryParam("next") String next, @QueryParam("error_path") String errorPath,
        @QueryParam("code") String code) throws URISyntaxException, JsonProcessingException {
    
        String authorizationUrl = authService.getAuthorizationUrl(next, code, errorPath);
        return Response.temporaryRedirect(new URI(authorizationUrl)).build();
    }
    
    @GET
    @Path("/callback")
    public Response callback(
        @QueryParam("error") String error, @QueryParam("code") String code,
        @QueryParam("state") String state
    ) throws CoffeeException {
        if (StringUtils.isNullOrEmpty(code)) {
            throw new BadRequest("Google Authentication not successful");
        }

        LoginResponse loginResponse = authService.createOrUpdateUser(code);

        return RestUtils.ok(loginResponse);
    }
}
