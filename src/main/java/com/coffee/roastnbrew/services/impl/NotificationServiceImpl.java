package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.app.AsyncActor;
import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daos.NotificationDAO;
import com.coffee.roastnbrew.daos.UsersDAO;
import com.coffee.roastnbrew.models.Product;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.models.feedbacks.Feedback;
import com.coffee.roastnbrew.models.feedbacks.Request;
import com.coffee.roastnbrew.models.notifications.Notification;
import com.coffee.roastnbrew.models.notifications.NotificationType;
import com.coffee.roastnbrew.services.NotificationService;
import com.coffee.roastnbrew.utils.ListUtils;
import com.coffee.roastnbrew.utils.StringUtils;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jvnet.hk2.annotations.Service;

@Service
@Singleton
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationDAO notificationDAO;
    private final UsersDAO usersDAO;
    
    @Inject
    public NotificationServiceImpl(NotificationDAO notificationDAO, UsersDAO usersDAO) {
        this.notificationDAO = notificationDAO;
        this.usersDAO = usersDAO;
    }
    
    public List<Notification> getNotifications(long userId, boolean unreadOnly) {
        return notificationDAO.getNotifications(userId, unreadOnly);
    }
    
    public boolean markNotificationsRead(List<Long> notificationIds) {
        if (ListUtils.isEmpty(notificationIds)) {
            return true;
        }
        return notificationDAO.markNotificationsRead(notificationIds);
    }
    
    public void sendNotificationOnNewUser(User newUser) {
        List<User> users = usersDAO.getAllUsers();
        for (User user: users) {
            if (user.equals(newUser)) {
                continue;
            }
            Notification notification = new Notification();
            
            notification.setUserId(user.getId());
            notification.setType(NotificationType.USER_JOINED);
            notification.setMessage(String.format("%s is now on Roast-n-Brew U+1F525 U+1F525",
                StringUtils.isNullOrEmpty(newUser.getFirstName()) ? newUser.getFirstName() : newUser.getEmailId()));
            notification.setFromId(Constants.SYSTEM_USER_ID);
            notification.setEntityId(newUser.getId());
            
            notificationDAO.createNotification(notification);
        }
    }

    public void sendGlobalNotification(String message) {
        AsyncActor.perform("Global Notification", () -> {
            List<User> users = usersDAO.getAllUsers();
            for (User user: users) {
                if (Constants.SYSTEM_USER_ID == user.getId()) {
                    continue;
                }
                Notification notification = new Notification();
        
                notification.setUserId(user.getId());
                notification.setType(NotificationType.GLOBAL_NOTIFICATION);
                notification.setMessage(message);
                notification.setFromId(Constants.SYSTEM_USER_ID);
                notification.setEntityId(user.getId());
        
                notificationDAO.createNotification(notification);
            }
        });
    }
    
    public void orderPlacedNotification(User user, Product product) {
        Notification notification = new Notification();
    
        notification.setUserId(user.getId());
        notification.setType(NotificationType.ORDER_PLACED);
        notification.setMessage(String.format("Hey %s! Your order for %s has been placed. "
                + "Someone will reach out to you soon regarding the status. ",
            StringUtils.isNullOrEmpty(user.getFirstName()) ?
                user.getFirstName() : user.getEmailId(), product.getName()));
        notification.setFromId(Constants.SYSTEM_USER_ID);
        notification.setEntityId(product.getId());
    
        notificationDAO.createNotification(notification);
    }

    public void feedbackRepliedNotification(Feedback feedback) {
        User sender = usersDAO.getById(feedback.getSenderId());
        User receiver = usersDAO.getById(feedback.getReceiverId());
        Notification notification = new Notification();
    
        notification.setUserId(sender.getId());
        notification.setType(NotificationType.FEEDBACK_REPLIED);
    
        notification.setMessage(String.format("Hey there %s! %s had this to say regarding your feedback: %s",
            StringUtils.isNullOrEmpty(sender.getFirstName()) ? sender.getFirstName() : sender.getEmailId(),
            StringUtils.isNullOrEmpty(receiver.getFirstName()) ? receiver.getFirstName() : receiver.getEmailId(),
            feedback.getReceiverReply()));
        
        notification.setFromId(receiver.getId());
        notification.setEntityId(receiver.getId());
    
        notificationDAO.createNotification(notification);
    }
    
    public void feedbackRequestNotification(Request request) {
        User requester = usersDAO.getById(request.getRequesterId());
        User requestee = usersDAO.getById(request.getRequesteeId());
        Notification notification = new Notification();
    
        notification.setUserId(requestee.getId());
        notification.setType(NotificationType.RECEIVED_REQUEST);
    
        String requesterName = StringUtils.isNullOrEmpty(requester.getFirstName()) ? requester.getEmailId() : requester.getFirstName();
        String requesteeName = StringUtils.isNullOrEmpty(requestee.getFirstName()) ? requestee.getEmailId() : requestee.getFirstName();
        
        notification.setMessage(String.format("Hey there %s! %s is requesting your feedback. %s also added, %s",
            requesteeName, requesterName, requesterName, request.getMessage()));
    
        notification.setFromId(request.getRequesterId());
        notification.setEntityId(request.getRequesterId());
    
        notificationDAO.createNotification(notification);
    }
    
    public void feedbackReceived(User sender, User receiver, Feedback feedback) {
        
        Notification notification = new Notification();
    
        notification.setUserId(receiver.getId());
        notification.setType(NotificationType.RECEIVED_FEEDBACK);
    
        String senderName = StringUtils.isNullOrEmpty(sender.getFirstName()) ? sender.getEmailId() : sender.getFirstName();
        String receiverName = StringUtils.isNullOrEmpty(receiver.getFirstName()) ? receiver.getEmailId() : receiver.getFirstName();
    
        String message = String.format("Hey there %s! %s has given you a feedback. ",
            receiverName, senderName);
        notification.setMessage(message);
    
        notification.setFromId(sender.getId());
        notification.setEntityId(sender.getId());
    
        notificationDAO.createNotification(notification);
    }
}
