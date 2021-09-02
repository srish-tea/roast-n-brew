package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.daos.NotificationDAO;
import com.coffee.roastnbrew.models.notifications.Notification;
import com.coffee.roastnbrew.services.NotificationService;
import com.coffee.roastnbrew.utils.ListUtils;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jvnet.hk2.annotations.Service;

@Service
@Singleton
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationDAO notificationDAO;
    
    @Inject
    public NotificationServiceImpl(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
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
}
