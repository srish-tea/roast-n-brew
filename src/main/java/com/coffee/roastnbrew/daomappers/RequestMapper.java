package com.coffee.roastnbrew.daomappers;

import com.coffee.roastnbrew.models.feedbacks.Request;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class RequestMapper implements RowMapper<Request> {
    
    @Override
    public Request map(ResultSet rs, StatementContext ctx) throws SQLException {
        Request request = new Request();
        request.setMessage(rs.getString("message"));
        request.setRequesteeId(rs.getLong("requestee_id"));
        request.setRequesterId(rs.getLong("requester_id"));
        return request;
    }
}
