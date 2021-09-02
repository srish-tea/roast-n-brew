package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.models.notifications.Notification;
import java.util.List;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface NotificationService {
    
    List<Notification> getNotifications(long userId, boolean unreadOnly);
    
    boolean markNotificationsRead(List<Long> notificationIds);
    
    void sendNotificationOnNewUser(User user);
    
    void sendGlobalNotification(String message);
}
