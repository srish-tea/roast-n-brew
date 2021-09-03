package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daos.FeedbackDAO;
import com.coffee.roastnbrew.daos.UsersDAO;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.Card;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.models.feedbacks.Feedback;
import com.coffee.roastnbrew.models.feedbacks.FeedbackDetailed;
import com.coffee.roastnbrew.services.FeedbackService;
import com.coffee.roastnbrew.services.NotificationService;
import com.coffee.roastnbrew.utils.StringUtils;
import java.util.List;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;

@Service
@Singleton
public class FeedbackServiceImpl implements FeedbackService {
    
    NotificationService notificationService;
    FeedbackDAO feedbackDAO;
    UsersDAO usersDAO;

    @Inject
    FeedbackServiceImpl(FeedbackDAO feedbackDAO, NotificationService notificationService, UsersDAO usersDAO) {
        this.feedbackDAO = feedbackDAO;
        this.notificationService = notificationService;
        this.usersDAO = usersDAO;
    }

    @Override
    public long giveFeedback(Feedback feedback) throws CoffeeException {
        feedback.setVisible(feedback.isPublic());
        
        List<Card> cards = feedback.getCards();
        int coinsIncrement = (int) (cards.size() * Constants.COIN_VALUE);
        User user = usersDAO.getById(feedback.getReceiverId());
        user.setCoinsBalance(user.getCoinsBalance() + coinsIncrement);
        usersDAO.updateUser(user);
        
        User senderUser = usersDAO.getById(feedback.getSenderId());
        notificationService.feedbackReceived(senderUser, user, feedback);
        
        return feedbackDAO.addFeedback(feedback);
    }

    @Override
    public boolean replyToFeedback(Feedback feedback) {
        if (StringUtils.isNullOrEmpty(feedback.getReceiverReply())) {
            return true;
        }
        boolean updated = feedbackDAO.replyToFeedback(feedback);
        notificationService.feedbackRepliedNotification(feedback);
        return updated;
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
        return feedbackDAO.getDetailedUserFeedbacks(userId, publicOnly, visibleOnly);
    }
}
