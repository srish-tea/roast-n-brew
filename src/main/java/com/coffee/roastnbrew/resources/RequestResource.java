package com.coffee.roastnbrew.resources;


import com.coffee.roastnbrew.models.feedbacks.Request;
import com.coffee.roastnbrew.services.RequestService;
import com.coffee.roastnbrew.utils.RestUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("/coffee/requests")
@Produces(MediaType.APPLICATION_JSON)
public class RequestResource {
    RequestService requestService;

    @Inject
    public RequestResource(RequestService requestService) {
        this.requestService = requestService;
    }

    @POST
    public Response sendRequest(Request request) {
        requestService.sendRequest(request);
        //send notification
        return RestUtils.noContentResponse();
    }

}
