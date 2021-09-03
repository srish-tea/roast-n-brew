package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.RequestMapper;
import com.coffee.roastnbrew.models.feedbacks.Request;
import com.google.inject.Singleton;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jvnet.hk2.annotations.Service;

@Slf4j
@Service
@Singleton
public class RequestDAO {
    
    private final Jdbi jdbi;
    
    @Inject
    public RequestDAO() {
        this.jdbi = Jdbi.create(Constants.DB_URL);
        this.jdbi.registerRowMapper(new RequestMapper());
    }
    
    public void addRequest(Request request) {
        this.jdbi.withHandle(handle -> {
            String query = "INSERT INTO request (`requester_id`, `requestee_id`, `message`) VALUES (:requester_id, :requestee_id, :message)";
            return handle.createUpdate(query)
                .bind("requester_id", request.getRequesterId())
                .bind("requestee_id", request.getRequesteeId())
                .bind("message", request.getMessage())
                .execute();
        });
    }
}
