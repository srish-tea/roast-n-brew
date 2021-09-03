package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.FeedbackMapper;
import com.coffee.roastnbrew.models.marketplace.Order;
import com.coffee.roastnbrew.utils.StringUtils;
import com.google.inject.Singleton;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.List;

@Service
@Singleton
public class OrderDAO {
    private final Jdbi jdbi;

    @Inject
    public OrderDAO() {
        this.jdbi = Jdbi.create(Constants.DB_URL);
        jdbi.registerRowMapper(new FeedbackMapper());
    }


    public Order getById(long orderId) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM orders WHERE id = :id";
            return handle.createQuery(query)
                    .bind(Constants.ID, orderId)
                    .mapTo(Order.class)
                    .findFirst()
                    .orElse(null)
                    ;
        });
    }

    public List<Order> getAll() {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM orders WHERE is_deleted = 0";
            return handle.createQuery(query)
                    .mapTo(Order.class)
                    .list();
        });
    }

    public long createOrder(Order order) {
        return this.jdbi.withHandle(handle -> {
            String query =
                    "INSERT INTO orders (user_id, "
                            + "product_id, status) "
                            + "VALUES (:user_id, "
                            + ":product_id, :status)";
            Update update = handle.createUpdate(query);
            update.bind("user_id", order.getUserId());
            update.bind("product_id", order.getProductId());
            update.bind("status", order.getStatus() );
            return update.executeAndReturnGeneratedKeys()
                    .mapTo(Long.class)
                    .one();
        });
    }

    public boolean updateOrder(Order order) {
        return this.jdbi.withHandle(handle -> {
            StringBuilder query = new StringBuilder("UPDATE orders SET ");

            if (!StringUtils.isNullOrEmpty(order.getStatus())) {
                query.append("status = :status ");
            }
            query.append("WHERE id = :id ");

            Update update = handle.createUpdate(query.toString());
            if (!StringUtils.isNullOrEmpty(order.getStatus())) {
                update.bind("status", order.getStatus());
            }
            update.bind("id", order.getId());

            return update.execute() == 1;
        });
    }
}
