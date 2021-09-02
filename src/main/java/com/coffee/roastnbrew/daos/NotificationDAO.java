package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.daomappers.NotificationMapper;
import com.coffee.roastnbrew.models.notifications.Notification;
import com.google.inject.Singleton;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
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
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM notifications WHERE user_id = :user_id ");
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
            String query = "UPDATE notifications SET is_read = true WHERE id in (<notification_ids>)";
            return handle.createUpdate(query)
                .bindList("notification_ids", notificationsIds)
                .execute() == notificationsIds.size();
        });
    }
}
