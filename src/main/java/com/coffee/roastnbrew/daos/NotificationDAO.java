package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.daomappers.NotificationMapper;
import com.coffee.roastnbrew.models.notifications.Notification;
import com.google.inject.Singleton;
import java.util.List;
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
        this.jdbi =  Jdbi.create("jdbc:mysql://localhost:3306/coffee?autoReconnect=true&user=root&password=pass&useSSL=false&useServerPrepStmts=false&allowPublicKeyRetrieval=false");
        jdbi.registerRowMapper(new NotificationMapper());
    }
    
    public List<Notification> getNotifications(long userId, boolean unreadOnly) {
        return this.jdbi.withHandle(handle -> {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM notification WHERE user_id = :user_id ");
            if (unreadOnly) {
                queryBuilder.append(" AND is_read = false");
            }
            queryBuilder.append(" ORDER BY id DESC");
            
            return handle.createQuery(queryBuilder.toString())
                .bind("user_id", userId)
                .mapTo(Notification.class)
                .list();
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
