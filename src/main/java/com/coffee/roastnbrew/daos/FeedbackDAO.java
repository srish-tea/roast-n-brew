package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.FeedbackMapper;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.Feedback;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.utils.JSONUtils;
import com.coffee.roastnbrew.utils.ListUtils;
import com.coffee.roastnbrew.utils.StringUtils;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class FeedbackDAO {
    private final Jdbi jdbi;

    @Inject
    public FeedbackDAO() {
        this.jdbi = Jdbi.create(Constants.DB_URL);
        jdbi.registerRowMapper(new FeedbackMapper());
    }

    public Feedback getById(long id) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM feedback WHERE id = :id";
            return handle.createQuery(query)
                    .bind(Constants.ID, id)
                    .mapTo(Feedback.class)
                    .findFirst()
                    .orElse(null)
                    ;
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

    public long addFeedback(Feedback feedback) throws IOException, CoffeeException {
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

    public List<Feedback> getUserFeedbacks(User user) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM feedback WHERE receiver_id = :receiver_id";
            return handle.createQuery(query)
                    .bind("receiver_id", user.getId())
                    .mapTo(Feedback.class)
                    .list();
        });
    }
}