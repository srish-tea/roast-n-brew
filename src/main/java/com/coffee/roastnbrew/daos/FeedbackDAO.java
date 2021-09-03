package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.FeedbackDetailedMapper;
import com.coffee.roastnbrew.daomappers.FeedbackMapper;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.feedbacks.Feedback;
import com.coffee.roastnbrew.models.feedbacks.FeedbackDetailed;
import com.coffee.roastnbrew.utils.JSONUtils;
import com.coffee.roastnbrew.utils.ListUtils;
import com.coffee.roastnbrew.utils.StringUtils;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class FeedbackDAO {
    private final Jdbi jdbi;

    @Inject
    public FeedbackDAO() {
        this.jdbi = Jdbi.create(Constants.DB_URL);
        jdbi.registerRowMapper(new FeedbackMapper());
        jdbi.registerRowMapper(new FeedbackDetailedMapper());
    }

    public FeedbackDetailed getById(long id) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT f.*, "
                + "       sndr.first_name  as sender_first_name, "
                + "       sndr.email_id    as sender_email_id, "
                + "       sndr.image_url   as sender_image_url, "
                + "       sndr.is_group    as sender_is_group, "
                + "       recvr.first_name as receiver_first_name, "
                + "       recvr.email_id   as receiver_email_id, "
                + "       recvr.image_url  as receiver_image_url, "
                + "       recvr.is_group   as receiver_is_group "
                + "FROM feedback f "
                + "         JOIN user sndr ON f.sender_id = sndr.id "
                + "         JOIN user recvr ON f.receiver_id = recvr.id "
                + " WHERE f.id = :id";
            return handle.createQuery(query)
                    .bind(Constants.ID, id)
                    .mapTo(FeedbackDetailed.class)
                    .findFirst()
                    .orElse(null)
                    ;
        });
    }
    
    public boolean replyToFeedback(Feedback feedback) {
        return this.jdbi.withHandle(handle -> {
            StringBuilder query = new StringBuilder("UPDATE feedback SET ");
            
            if (!StringUtils.isNullOrEmpty(feedback.getReceiverReply())) {
                query.append("receiver_reply = :receiver_reply, ");
            }
            query.append("WHERE id = :id ");
            
            Update update = handle.createUpdate(query.toString());
            if (!StringUtils.isNullOrEmpty(feedback.getReceiverReply())) {
                update.bind("receiver_reply", feedback.getReceiverReply());
            }
            update.bind("id", feedback.getId());
            
            return update.execute() == 1;
        });
    }
    
    public boolean updateFeedback(Feedback feedback) {
        return this.jdbi.withHandle(handle -> {
            StringBuilder query = new StringBuilder("UPDATE feedback SET ");

            if (!StringUtils.isNullOrEmpty(feedback.getReceiverReply())) {
                query.append("receiver_reply = :receiver_reply, ");
            }
            query.append("is_visible = :is_visible ");
            query.append("WHERE id = :id ");

            Update update = handle.createUpdate(query.toString());
            if (!StringUtils.isNullOrEmpty(feedback.getReceiverReply())) {
                update.bind("receiver_reply", feedback.getReceiverReply());
            }
            update.bind("is_visible", feedback.isVisible());
            update.bind("id", feedback.getId());

            return update.execute() == 1;
        });
    }

    public long addFeedback(Feedback feedback) throws CoffeeException {
        return this.jdbi.withHandle(handle -> {
            String query =
                    "INSERT INTO feedback (sender_id, receiver_id, "
                            + "is_anonymous, is_public, content,  "
                            + "cards, coins, is_visible, "
                            + "receiver_reply) VALUES "
                            + "(:sender_id, :receiver_id, "
                            + ":is_anonymous, :is_public, :content,  "
                            + ":cards, :coins, :is_visible, "
                            + ":receiver_reply)";
            Update update = handle.createUpdate(query);
            update.bind("sender_id", feedback.getSenderId());
            update.bind("receiver_id", feedback.getReceiverId());
            update.bind("is_anonymous", feedback.isAnonymous());
            update.bind("is_public", feedback.isPublic());
            update.bind("content", JSONUtils.objectToJsonString(feedback.getContent()));

            update.bind("cards",
                    ListUtils.isEmpty(feedback.getCards()) ? "" :
                            feedback.getCards().stream().map(Enum::toString).collect(Collectors.joining(",")));
            update.bind("coins", feedback.getCoins());
            update.bind("is_visible", feedback.isVisible());
            update.bind("receiver_reply", feedback.getReceiverReply());
            return update.executeAndReturnGeneratedKeys()
                    .mapTo(Long.class)
                    .one();
        });
    }
    
    public List<Feedback> getFeedbacksForUser(long userId) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM feedback WHERE receiver_id = :user_id AND is_deleted = false";
            return handle.createQuery(query)
                .bind("user_id", userId)
                .mapTo(Feedback.class)
                .list();
        });
    }

    public List<FeedbackDetailed> getDetailedUserFeedbacks(Long senderId, Long receiverId, boolean publicOnly, boolean visibleOnly) {
        return this.jdbi.withHandle(handle -> {
            StringBuilder query = new StringBuilder(""
                + "SELECT f.*, "
                + "       sndr.first_name  as sender_first_name, "
                + "       sndr.email_id    as sender_email_id, "
                + "       sndr.image_url   as sender_image_url, "
                + "       sndr.is_group    as sender_is_group, "
                + "       recvr.first_name as receiver_first_name, "
                + "       recvr.email_id   as receiver_email_id, "
                + "       recvr.image_url  as receiver_image_url, "
                + "       recvr.is_group   as receiver_is_group "
                + "FROM feedback f "
                + "         JOIN user sndr ON f.sender_id = sndr.id "
                + "         JOIN user recvr ON f.receiver_id = recvr.id");
            if (receiverId != null) {
                query.append(" WHERE receiver_id = :receiver_id");
            }
            if (senderId != null) {
                query.append(" WHERE sender_id = :sender_id");
            }
            
            if (publicOnly) {
                query.append(" AND is_public = true ");
            }
            if (visibleOnly) {
                query.append(" AND is_visible = true ");
            }

            query.append(" ORDER BY f.id DESC");
            Query queryObj = handle.createQuery(query.toString());
            if (receiverId != null) {
                queryObj.bind("receiver_id", receiverId);
            }
            if (senderId != null) {
                queryObj.bind("sender_id", senderId);
            }
            return queryObj.mapTo(FeedbackDetailed.class).list();
        });
    }
}
