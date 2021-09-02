package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.UserMapper;
import com.coffee.roastnbrew.models.Feedback;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FeedbackDAO {
    private final Jdbi jdbi;

    @Inject
    public FeedbackDAO() {
        this.jdbi =  Jdbi.create("jdbc:mysql://localhost:3306/coffee?autoReconnect=true&user=root&password=pass&useSSL=false&useServerPrepStmts=false&allowPublicKeyRetrieval=false");
        jdbi.registerRowMapper(new UserMapper());
    }

    public Feedback getById(int id) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM users WHERE id = :id";
            return handle.createQuery(query)
                    .bind(Constants.ID, id)
                    .mapTo(Feedback.class)
                    .findFirst()
                    .orElse(null)
                    ;
        });
    }
}
