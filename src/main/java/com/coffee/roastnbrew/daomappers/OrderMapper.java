package com.coffee.roastnbrew.daomappers;

import com.coffee.roastnbrew.models.marketplace.Order;
import com.coffee.roastnbrew.models.marketplace.OrderStatus;
import com.coffee.roastnbrew.utils.StringUtils;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order map(ResultSet rs, StatementContext ctx) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setProductId(rs.getLong("product_id"));
        order.setStatus(StringUtils.isNullOrEmpty(rs.getString("status")) ? OrderStatus.ORDERED
                : OrderStatus.valueOf(rs.getString("status").trim()));
        order.setCreatedTs(rs.getTimestamp("created_ts").getTime());
        order.setUpdatedTs(rs.getTimestamp("updated_ts").getTime());
        order.setDeleted(rs.getBoolean("is_deleted"));
        return order;
    }
}
