package com.coffee.roastnbrew.daomappers;

import com.coffee.roastnbrew.models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.coffee.roastnbrew.utils.StringUtils;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class UserMapper implements RowMapper<User> {
    
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmailId(rs.getString("email_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setBio(rs.getString("bio"));
        user.setImageUrl(rs.getString("image_url"));
        user.setDesignation(rs.getString("designation"));
        user.setLocation(rs.getString("location"));
        
        String canTalkAbout = rs.getString("can_talk_about");
        user.setCanTalkAbout(!StringUtils.isNullOrEmpty(canTalkAbout) ?
            Arrays.stream(canTalkAbout.split(",")).collect(Collectors.toList()) :
            new ArrayList<>());
        
        String cannotTalkAbout = rs.getString("cannot_talk_about");
        user.setCannotTalkAbout(!StringUtils.isNullOrEmpty(cannotTalkAbout) ?
            Arrays.stream(cannotTalkAbout.split(",")).collect(Collectors.toList()) :
            new ArrayList<>());

        user.setCoinsBalance(rs.getInt("coins_balance"));
        user.setGroup(rs.getBoolean("is_group"));
        user.setCreatedTs(rs.getTimestamp("created_ts").getTime());
        user.setUpdatedTs(rs.getTimestamp("updated_ts").getTime());
        user.setDeleted(rs.getBoolean("is_deleted"));
        return user;
    }
}
