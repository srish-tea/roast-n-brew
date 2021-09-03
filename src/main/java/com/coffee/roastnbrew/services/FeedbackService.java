package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.feedbacks.Feedback;
import com.coffee.roastnbrew.models.feedbacks.FeedbackDetailed;
import java.util.List;
import org.jvnet.hk2.annotations.Contract;

import java.io.IOException;

@Contract
public interface FeedbackService {

    long giveFeedback(Feedback feedback) throws IOException, CoffeeException;

    boolean updateFeedback(Feedback feedback);
    
    FeedbackDetailed getFeedbackById(long id);

    List<FeedbackDetailed> getFeedbacks(long userId, boolean publicOnly, boolean visibleOnly);
}
