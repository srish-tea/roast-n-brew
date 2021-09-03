package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.NotificationMapper;
import com.coffee.roastnbrew.models.Entity;
import com.coffee.roastnbrew.models.notifications.Notification;
import com.coffee.roastnbrew.models.notifications.NotificationType;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;
import org.jvnet.hk2.annotations.Service;

@Slf4j
@Service
@Singleton
public class NotificationDAO {
    
    private final Jdbi jdbi;
    
    @Inject
    public NotificationDAO() {
        this.jdbi =  Jdbi.create(Constants.DB_URL);
        jdbi.registerRowMapper(new NotificationMapper());
    }
    
    public List<Notification> getNotifications(long userId, boolean unreadOnly) {
        return this.jdbi.withHandle(handle -> {
            
            List<Notification> notifications = new ArrayList<>();
            for (Entry<String, List<NotificationType>> entry: NotificationType.entityToTypeMap.entrySet()) {
                StringBuilder queryBuilder = new StringBuilder(String
                    .format("SELECT *, e.image_url as entity_image_url from notification n join %s e on "
                            + "n.entity_id = e.id AND n.type in (<types>) AND n.user_id = :user_id ",
                        entry.getKey()));
                if (unreadOnly) {
                    queryBuilder.append(" AND is_read = false");
                }
                queryBuilder.append(" ORDER BY n.id DESC");

                notifications.addAll(handle.createQuery(queryBuilder.toString())
                    .bind("user_id", userId)
                    .bindList("types", entry.getValue())
                    .mapTo(Notification.class)
                    .list());
            }
            notifications.sort(Comparator.comparing(Entity::getCreatedTs).reversed());
            return notifications;
        });
    }
    
    public boolean markNotificationsRead(List<Long> notificationsIds) {
        return this.jdbi.withHandle(handle -> {
            String query = "UPDATE notification SET is_read = true WHERE id in (<notification_ids>)";
            return handle.createUpdate(query)
                .bindList("notification_ids", notificationsIds)
                .execute() == notificationsIds.size();
        });
    }

    public long createNotification(Notification notification) {
        return this.jdbi.withHandle(handle -> {
            String query = "INSERT INTO notification (user_id, type, message, entity_id, from_id) "
                + "VALUES (:user_id, :type, :message, :entity_id, :from_id)";
            Update update = handle.createUpdate(query);
            update.bind("user_id", notification.getUserId());
            update.bind("type", notification.getType());
            update.bind("message", notification.getMessage());
            update.bind("entity_id", notification.getEntityId());
            update.bind("from_id", notification.getFromId());
            return update.executeAndReturnGeneratedKeys()
                .mapTo(Long.class)
                .one();
        });
    }
}
