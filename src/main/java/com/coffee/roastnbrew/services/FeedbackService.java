package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.Feedback;
import org.jvnet.hk2.annotations.Contract;

import java.io.IOException;

@Contract
public interface FeedbackService {

    long giveFeedback(Feedback feedback) throws IOException, CoffeeException;

    boolean updateFeedback(Feedback feedback);

    Feedback getFeedbackById(long id);
}
