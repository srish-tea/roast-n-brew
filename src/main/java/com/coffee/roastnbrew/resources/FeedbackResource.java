package com.coffee.roastnbrew.resources;

import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.Feedback;
import com.coffee.roastnbrew.services.FeedbackService;
import com.coffee.roastnbrew.utils.RestUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/coffee/feedbacks")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class FeedbackResource {
    FeedbackService feedbackService;

    @Inject
    public FeedbackResource(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @POST
    public Response giveFeedback(Feedback feedback) throws IOException, CoffeeException {
        long feedbackId = feedbackService.giveFeedback(feedback);
        //send notification
        return RestUtils.ok(feedbackService.getFeedbackById(feedbackId));
    }

    @PUT
    public Response updateFeedback(Feedback feedback) {
         feedbackService.updateFeedback(feedback);
        //send notification
        return RestUtils.ok(feedbackService.getFeedbackById(feedback.getId()));
    }

    @GET
    @Path("/{id}")
    public Response getFeedback(@PathParam("id") Long id) throws IOException {
        return RestUtils.ok(feedbackService.getFeedbackById(id));
    }
}
