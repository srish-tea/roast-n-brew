package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.FeedbackMapper;
import com.coffee.roastnbrew.models.Feedback;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.utils.ListUtils;
import org.jdbi.v3.core.Jdbi;
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
    }

    public Feedback getById(int id) {
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

    public int addFeedback(Feedback feedback) {
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
            update.bind("content", feedback.getContent().toString());
            update.bind("cards",
                    ListUtils.isEmpty(feedback.getCards()) ? "" :
                            feedback.getCards().stream().map(Enum::toString).collect(Collectors.joining(",")));
            update.bind("coins", feedback.getCoins());
            update.bind("is_visible", feedback.isVisible());
            update.bind("receiver_reply", feedback.getReceiverReply());
            return update.executeAndReturnGeneratedKeys()
                    .mapTo(Integer.class)
                    .one();
        });
    }

    public List<Feedback> getUsersFeedback(User user) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM feedback WHERE receiver_id = :receiver_id";
            return handle.createQuery(query)
                    .bind("receiver_id", user.getId())
                    .mapTo(Feedback.class)
                    .list();
        });
    }
}
