package com.coffee.roastnbrew.resources;

import com.coffee.roastnbrew.services.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class UserResource {

    UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int userId) {
        return Response.ok(userService.getUser(userId).toString()).build();
    }
}
