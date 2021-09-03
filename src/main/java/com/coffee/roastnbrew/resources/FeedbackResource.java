package com.coffee.roastnbrew.resources;

import com.coffee.roastnbrew.dtos.BadResponse;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.feedbacks.Feedback;
import com.coffee.roastnbrew.services.FeedbackService;
import com.coffee.roastnbrew.services.SentimentAnalysisService;
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
    SentimentAnalysisService sentimentAnalysisService;

    @Inject
    public FeedbackResource(FeedbackService feedbackService, SentimentAnalysisService sentimentAnalysisService) {
        this.feedbackService = feedbackService;
        this.sentimentAnalysisService = sentimentAnalysisService;
    }

    @GET
    public Response getFeedbacks(@QueryParam("user_id") long receiverId,
        @QueryParam("public_only") @DefaultValue("false") boolean publicOnly,
        @QueryParam("visible_only") @DefaultValue("false") boolean visibleOnly) {
        return RestUtils.ok(feedbackService.getFeedbacks(receiverId, publicOnly, visibleOnly));
    }

    @GET
    @Path("/given")
    public Response getGivenFeedbacks(@QueryParam("user_id") long senderId,
                                 @QueryParam("public_only") @DefaultValue("false") boolean publicOnly,
                                 @QueryParam("visible_only") @DefaultValue("false") boolean visibleOnly) {
        return RestUtils.ok(feedbackService.getGivenFeedbacks(senderId, publicOnly, visibleOnly));
    }


    @POST
    public Response giveFeedback(Feedback feedback) throws IOException, CoffeeException {
        boolean positive = sentimentAnalysisService.isPositiveContent(feedback.getContent());
        if (!positive) {
            return RestUtils.ok(BadResponse.builder().message("Haw! That's rude. We would like you to sound nice.")
                    .code(Response.Status.BAD_REQUEST.getStatusCode()).build());
        }
        long feedbackId = feedbackService.giveFeedback(feedback);
        //send notification
        return RestUtils.ok(feedbackService.getFeedbackById(feedbackId));
    }
    
    @PUT
    public Response updateFeedback(Feedback feedback) {
        feedbackService.updateFeedback(feedback);
        return RestUtils.ok(feedbackService.getFeedbackById(feedback.getId()));
    }

    @PUT
    @Path("/reply")
    public Response replyToFeedback(Feedback feedback) {
        feedbackService.replyToFeedback(feedback);
        return RestUtils.ok(feedbackService.getFeedbackById(feedback.getId()));
    }

    @GET
    @Path("/{id}")
    public Response getFeedback(@PathParam("id") Long id) throws IOException {
        return RestUtils.ok(feedbackService.getFeedbackById(id));
    }
}
