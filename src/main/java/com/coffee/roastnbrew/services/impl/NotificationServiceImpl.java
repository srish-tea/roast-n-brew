package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daos.NotificationDAO;
import com.coffee.roastnbrew.daos.UsersDAO;
import com.coffee.roastnbrew.models.User;
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
}
