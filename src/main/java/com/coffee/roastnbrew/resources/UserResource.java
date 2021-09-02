package com.coffee.roastnbrew.resources;

import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.services.UserService;

import com.coffee.roastnbrew.utils.RestUtils;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/coffee/users")
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
    public Response getUserById(@PathParam("id") int userId) {
        return RestUtils.ok(userService.getUserById(userId));
    }

    @GET
    public Response getAllUsers() {
        return RestUtils.ok(userService.getAllUsers());
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") long userId, User user) {
        return RestUtils.ok(userService.updateUser(user));
    }

    @PUT
    @Path("/add")
    public Response addNewUser(User user) {
        return RestUtils.ok(userService.addUser());
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") int userId) {
        return RestUtils.ok(userService.deleteUser(userId));
    }
}
