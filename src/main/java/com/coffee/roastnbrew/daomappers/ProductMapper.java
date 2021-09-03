package com.coffee.roastnbrew.daomappers;

import com.coffee.roastnbrew.models.Product;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {
    @Override
    public Product map(ResultSet rs, StatementContext ctx) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getInt("price"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCount(rs.getInt("count"));
        product.setCreatedTs(rs.getTimestamp("created_ts").getTime());
        product.setUpdatedTs(rs.getTimestamp("updated_ts").getTime());
        product.setDeleted(rs.getBoolean("is_deleted"));
        return product;
    }
}
