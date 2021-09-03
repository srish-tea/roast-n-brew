package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.app.AsyncActor;
import com.coffee.roastnbrew.daos.FeedbackDAO;
import com.coffee.roastnbrew.models.Card;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.daos.UsersDAO;
import com.coffee.roastnbrew.models.feedbacks.Feedback;
import com.coffee.roastnbrew.services.NotificationService;
import com.coffee.roastnbrew.services.UserService;
import java.util.EnumMap;
import java.util.List;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;

@Service
@Singleton
public class UserServiceImpl implements UserService {
    
    private final UsersDAO usersDAO;
    private final NotificationService notificationService;
    private final FeedbackDAO feedbackDAO;
    
    @Inject
    public UserServiceImpl(UsersDAO usersDAO,
        NotificationService notificationService,
        FeedbackDAO feedbackDAO) {
        this.usersDAO = usersDAO;
        this.notificationService = notificationService;
        this.feedbackDAO = feedbackDAO;
    }

    public User getUserById(long userId, boolean withCardDetails) {
        User user = usersDAO.getById(userId);
        if (withCardDetails) {
            List<Feedback> feedbacks = feedbackDAO.getFeedbacksForUser(userId);
            EnumMap<Card, Integer> cardCounts = new EnumMap<>(Card.class);
            feedbacks.forEach(feedback -> {
                for (Card card: feedback.getCards()) {
                    Integer count = cardCounts.getOrDefault(card, 0) + 1;
                    cardCounts.put(card, count);
                }
            });
            user.setCardCounts(cardCounts);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return usersDAO.getAllUsers();
    }

    @Override
    public User updateUser(User user) {
        usersDAO.updateUser(user);
        return getUserById(user.getId(), false);
    }

    @Override
    public User addUser(User user) {
        int userId = usersDAO.addUser(user);
        User newUser = getUserById(userId, false);
    
        AsyncActor.perform("New User", () -> notificationService.sendNotificationOnNewUser(newUser));

        return newUser;
    }
    
    @Override
    public boolean deleteUser(long userId) {
        return usersDAO.deleteUser(userId);
    }
}
