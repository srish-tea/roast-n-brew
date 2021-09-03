package com.coffee.roastnbrew.daomappers;

import com.coffee.roastnbrew.models.Card;
import com.coffee.roastnbrew.models.feedbacks.Feedback;
import com.coffee.roastnbrew.models.feedbacks.FeedbackContent;
import com.coffee.roastnbrew.utils.JSONUtils;
import com.coffee.roastnbrew.utils.StringUtils;
import lombok.SneakyThrows;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FeedbackMapper implements RowMapper<Feedback> {
    @SneakyThrows
    @Override
    public Feedback map(ResultSet rs, StatementContext ctx) throws SQLException {

        Feedback feedback = new Feedback();
        feedback.setId(rs.getInt("id"));
        feedback.setSenderId(rs.getLong("sender_id"));
        feedback.setReceiverId(rs.getLong("receiver_id"));
        feedback.setAnonymous(rs.getBoolean("is_anonymous"));
        feedback.setPublic(rs.getBoolean("is_public"));
        feedback.setContent(JSONUtils.fromJson(rs.getString("content"), FeedbackContent.class));

        String cards = rs.getString("cards");
                feedback.setCards(!StringUtils.isNullOrEmpty(cards) ?
                        Arrays.stream(cards.split(",")).map(Card::valueOf).collect(Collectors.toList()) :
                        new ArrayList<>());

        feedback.setCoins(rs.getInt("coins"));
        feedback.setVisible(rs.getBoolean("is_visible"));
        feedback.setReceiverReply(rs.getString("receiver_reply"));
        feedback.setCreatedTs(rs.getTimestamp("created_ts").getTime());
        feedback.setUpdatedTs(rs.getTimestamp("updated_ts").getTime());
        feedback.setDeleted(rs.getBoolean("is_deleted"));
        return feedback;
    }
}
