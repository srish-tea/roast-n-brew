package com.coffee.roastnbrew.resources;

import com.coffee.roastnbrew.services.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    public String getAllUsers() {
        return userService.getAllUsers();
    }

    @Path("/{id}")
    @GET
    public String getUserById(@PathParam("id") int userId) {
        return userService.getUserById(userId);
    }

    /*
    @Path("/{id}")
    @PUT
    public String updateUser(@PathParam("id") long userId, User user) {
        return userService.updateUser();
    }

    @Path("/add")
    @PUT
    public String addNewUser(User user) {
        return userService.addUser();
    }*/
}
