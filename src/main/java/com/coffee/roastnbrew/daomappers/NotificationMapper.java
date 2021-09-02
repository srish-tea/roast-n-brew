package com.coffee.roastnbrew.daomappers;

import com.coffee.roastnbrew.models.notifications.Notification;
import com.coffee.roastnbrew.models.notifications.NotificationType;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class NotificationMapper implements RowMapper<Notification> {
    
    @Override
    public Notification map(ResultSet rs, StatementContext ctx) throws SQLException {
        Notification notification = new Notification();
    
        notification.setId(rs.getLong("id"));
        notification.setUserId(rs.getLong("user_id"));
        notification.setType(NotificationType.valueOf(rs.getString("type")));
        notification.setMessage(rs.getString("message"));
        notification.setRead(rs.getBoolean("is_read"));
        notification.setEntityId(rs.getLong("entity_id"));
        notification.setFromId(rs.getLong("from_id"));
        notification.setCreatedTs(rs.getTimestamp("created_ts").getTime());
        notification.setUpdatedTs(rs.getTimestamp("updated_ts").getTime());
        notification.setDeleted(rs.getBoolean("is_deleted"));
        
        return notification;
    }
}
