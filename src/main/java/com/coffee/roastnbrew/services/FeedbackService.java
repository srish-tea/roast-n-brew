package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.models.Feedback;
import com.coffee.roastnbrew.models.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface FeedbackService {

    void giveFeedback(User user, Feedback feedback);

    void updateFeedback(Feedback feedback);
}
