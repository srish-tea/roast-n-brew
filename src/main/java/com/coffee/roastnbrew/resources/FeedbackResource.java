package com.coffee.roastnbrew.resources;

import com.coffee.roastnbrew.models.Feedback;
import com.coffee.roastnbrew.models.Product;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.services.FeedbackService;
import com.coffee.roastnbrew.services.UserService;
import com.coffee.roastnbrew.utils.RestUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public Response giveFeedback(User user, Feedback feedback) {
        feedbackService.giveFeedback(user, feedback);
        //send notification
        return RestUtils.noContentResponse();
    }

    @PUT
    public Response updateFeedback(Feedback feedback) {
        feedbackService.updateFeedback(feedback);
        //send notification
        return RestUtils.noContentResponse();
    }
}
