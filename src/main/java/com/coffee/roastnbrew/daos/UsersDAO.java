package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.app.CoffeeConfig;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.UserMapper;
import com.coffee.roastnbrew.utils.ListUtils;
import com.coffee.roastnbrew.utils.StringUtils;
import com.google.inject.Singleton;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;
import org.jvnet.hk2.annotations.Service;

@Slf4j
@Service
@Singleton
public class UsersDAO {
    
    private final Jdbi jdbi;

    @Inject
    public UsersDAO() {
        this.jdbi = Jdbi.create("jdbc:mysql://10.0.18.58:3306/coffee?autoReconnect=true&user=root&password=rootROOT&useSSL=false&useServerPrepStmts=false&allowPublicKeyRetrieval=false");
        jdbi.registerRowMapper(new UserMapper());
    }
    
    public User getById(long userId) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM user WHERE id = :user_id";
            return handle.createQuery(query)
                .bind(Constants.USER_ID, userId)
                .mapTo(User.class)
                .findFirst()
                .orElse(null)
                ;
        });
    }
    
    public User getByEmail(String email) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM user WHERE email_id = :email_id";
            return handle.createQuery(query)
                .bind("email_id", email)
                .mapTo(User.class)
                .findFirst()
                .orElse(null)
                ;
        });
    }

    public int addUser(User user) {
        return this.jdbi.withHandle(handle -> {
            String query =
                "INSERT INTO user (email_id, first_name, last_name, "
                    + "image_url, designation, location, "
                    + "bio, can_talk_about, cannot_talk_about, "
                    + "coins_balance, is_group) VALUES "
                    + "(:email_id, :first_name, :last_name, "
                    + ":image_url, :designation, :location, "
                    + ":bio, :can_talk_about, :cannot_talk_about, "
                    + ":coins_balance, :is_group)";
            Update update = handle.createUpdate(query);
            update.bind("email_id", user.getEmailId());
            update.bind("first_name", user.getFirstName());
            update.bind("last_name", user.getLastName());
            update.bind("image_url", user.getImageUrl());
            update.bind("designation", user.getDesignation());
            update.bind("location", user.getLocation());
            update.bind("bio", user.getBio());
            update.bind("can_talk_about",
                ListUtils.isEmpty(user.getCanTalkAbout()) ? "" : String.join(",", user.getCanTalkAbout()));
            update.bind("cannot_talk_about",
                ListUtils.isEmpty(user.getCannotTalkAbout()) ? "" : String.join(",", user.getCannotTalkAbout()));
            update.bind("coins_balance", user.getCoinsBalance());
            update.bind("is_group", user.isGroup());
            return update.executeAndReturnGeneratedKeys()
                .mapTo(Integer.class)
                .one();
        });
    }
    
    public List<User> getAllUsers() {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM user WHERE is_deleted = 0";
            return handle.createQuery(query)
                .mapTo(User.class)
                .list();
        });
    }
    
    public boolean updateUser(User user) {
        return this.jdbi.withHandle(handle -> {
            StringBuilder query = new StringBuilder("UPDATE user SET ");
            if (StringUtils.isNullOrEmpty(user.getEmailId())) {
                query.append("email_id = :email_id, ");
            }
            if (StringUtils.isNullOrEmpty(user.getFirstName())) {
                query.append("fist_name = :first_name, ");
            }
            if (StringUtils.isNullOrEmpty(user.getLastName())) {
                query.append("last_name = :last_name, ");
            }
            if (StringUtils.isNullOrEmpty(user.getImageUrl())) {
                query.append("image_url = :image_url, ");
            }
            if (StringUtils.isNullOrEmpty(user.getDesignation())) {
                query.append("designation = :designation, ");
            }
            if (StringUtils.isNullOrEmpty(user.getLocation())) {
                query.append("location = :location, ");
            }
            if (StringUtils.isNullOrEmpty(user.getBio())) {
                query.append("bio = :bio, ");
            }
            if (!ListUtils.isEmpty(user.getCanTalkAbout())) {
                query.append("can_talk_about = :can_talk_about, ");
            }
            if (ListUtils.isEmpty(user.getCannotTalkAbout())) {
                query.append("cannot_talk_about = :cannot_talk_about ");
            }
            query.append("is_group = :is_group, ");
            query.append("coins_balance = :coins_balance ");
            
            Update update = handle.createUpdate(query.toString());
            if (StringUtils.isNullOrEmpty(user.getEmailId())) {
                update.bind("email_id", user.getEmailId());
            }
            if (StringUtils.isNullOrEmpty(user.getFirstName())) {
                update.bind("fist_name", user.getFirstName());
            }
            if (StringUtils.isNullOrEmpty(user.getLastName())) {
                update.bind("last_name", user.getLastName());
            }
            if (StringUtils.isNullOrEmpty(user.getImageUrl())) {
                update.bind("image_url", user.getImageUrl());
            }
            if (StringUtils.isNullOrEmpty(user.getDesignation())) {
                update.bind("designation", user.getDesignation());
            }
            if (StringUtils.isNullOrEmpty(user.getLocation())) {
                update.bind("location", user.getLocation());
            }
            if (StringUtils.isNullOrEmpty(user.getBio())) {
                update.bind("bio", user.getBio());
            }
            if (!ListUtils.isEmpty(user.getCanTalkAbout())) {
                update.bind("can_talk_about", String.join(",", user.getCanTalkAbout()));
            }
            if (ListUtils.isEmpty(user.getCannotTalkAbout())) {
                update.bind("cannot_talk_about", String.join(",", user.getCannotTalkAbout()));
            }
            update.bind("is_group", user.isGroup());
            update.bind("coins_balance", user.getCoinsBalance());
            return update.execute() == 1;
        });
    }

    public boolean deleteUser(long userId) {
        return this.jdbi.withHandle(handle -> {
            String query = "UPDATE user SET is_deleted = 1 where id = :user_id";
            return handle.createUpdate(query)
                .bind("user_id", userId)
                .execute() == 1;
        });
    }
}
