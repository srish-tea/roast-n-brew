package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.daos.FeedbackDAO;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.feedbacks.Feedback;
import com.coffee.roastnbrew.models.feedbacks.FeedbackDetailed;
import com.coffee.roastnbrew.services.FeedbackService;
import java.util.List;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Service
@Singleton
public class FeedbackServiceImpl implements FeedbackService {
    FeedbackDAO feedbackDAO;

    @Inject
    FeedbackServiceImpl(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    @Override
    public long giveFeedback(Feedback feedback) throws IOException, CoffeeException {
        return feedbackDAO.addFeedback(feedback);
    }

    @Override
    public boolean updateFeedback(Feedback feedback) {
        return feedbackDAO.updateFeedback(feedback);
    }

    @Override
    public FeedbackDetailed getFeedbackById(long id) {
        return feedbackDAO.getById(id);
    }
    
    @Override
    public List<FeedbackDetailed> getFeedbacks(long userId, boolean publicOnly, boolean visibleOnly) {
        return feedbackDAO.getUserFeedbacks(userId, publicOnly, visibleOnly);
    }
}
