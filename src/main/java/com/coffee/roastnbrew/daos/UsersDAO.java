package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.User;
import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.UserMapper;
import com.google.inject.Singleton;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jvnet.hk2.annotations.Service;

@Slf4j
@Service
@Singleton
public class UsersDAO {
    
    private final Jdbi jdbi;

    @Inject
    public UsersDAO() {
        this.jdbi =  Jdbi.create("jdbc:mysql://localhost:3306/coffee?autoReconnect=true&user=root&password=pass&useSSL=false&useServerPrepStmts=false&allowPublicKeyRetrieval=false");
        jdbi.registerRowMapper(new UserMapper());
    }
    
    public User getById(int userId) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM users WHERE id = :user_id";
            return handle.createQuery(query)
                .bind(Constants.USER_ID, userId)
                .mapTo(User.class)
                .findFirst()
                .orElse(null)
                ;
        });
    }
    
    
}
