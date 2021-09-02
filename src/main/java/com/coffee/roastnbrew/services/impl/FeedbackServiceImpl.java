package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.models.Feedback;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.services.FeedbackService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;

@Service
@Singleton
public class FeedbackServiceImpl implements FeedbackService {

    @Override
    public void giveFeedback(User user, Feedback feedback) {

    }

    @Override
    public void updateFeedback(Feedback feedback) {

    }
}
