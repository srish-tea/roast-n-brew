package com.coffee.roastnbrew.daos;

import com.coffee.roastnbrew.constants.Constants;
import com.coffee.roastnbrew.daomappers.FeedbackMapper;
import com.coffee.roastnbrew.models.Product;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.utils.StringUtils;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Service
@Singleton
public class ProductDAO {
    private final Jdbi jdbi;

    @Inject
    public ProductDAO() {
        this.jdbi = Jdbi.create(Constants.DB_URL);
        jdbi.registerRowMapper(new FeedbackMapper());
    }

    public Product getById(long productId) {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM product WHERE id = :id";
            return handle.createQuery(query)
                    .bind(Constants.ID, productId)
                    .mapTo(Product.class)
                    .findFirst()
                    .orElse(null)
                    ;
        });
    }

    public List<Product> getAll() {
        return this.jdbi.withHandle(handle -> {
            String query = "SELECT * FROM product WHERE is_deleted = 0";
            return handle.createQuery(query)
                    .mapTo(Product.class)
                    .list();
        });
    }

    public long addProduct(Product product) {
        return this.jdbi.withHandle(handle -> {
            String query =
                    "INSERT INTO product (name, price, "
                            + "image_url, count) "
                            + "VALUES (:name, :price, "
                            + ":image_url, :count)";
            Update update = handle.createUpdate(query);
            update.bind("name", product.getName());
            update.bind("price", product.getPrice());
            update.bind("image_url", product.getImageUrl());
            update.bind("count", product.getCount());
            return update.executeAndReturnGeneratedKeys()
                    .mapTo(Long.class)
                    .one();
        });
    }

    public boolean updateProduct(Product product) {
        return this.jdbi.withHandle(handle -> {
            StringBuilder query = new StringBuilder("UPDATE product SET ");

            if (!StringUtils.isNullOrEmpty(product.getPrice())) {
                query.append("price = :price, ");
            }
            if (!StringUtils.isNullOrEmpty(product.getCount())) {
                query.append("count = :count, ");
            }
            if (!StringUtils.isNullOrEmpty(product.getImageUrl())) {
                query.append("image_url = :image_url ");
            }
            query.append("WHERE id = :id ");

            Update update = handle.createUpdate(query.toString());
            if (!StringUtils.isNullOrEmpty(product.getPrice())) {
                update.bind("is_visible", product.getPrice());
            }
            if (!StringUtils.isNullOrEmpty(product.getCount())) {
                update.bind("is_visible", product.getCount());
            }
            if (!StringUtils.isNullOrEmpty(product.getImageUrl())) {
                update.bind("is_visible", product.getImageUrl());
            }

            update.bind("id", product.getId());

            return update.execute() == 1;
        });
    }

    public boolean decreaseProductCount(Product product, int orderQuantity) {
        return this.jdbi.withHandle(handle -> {
            StringBuilder query = new StringBuilder("UPDATE product SET count = :count WHERE id = :product_id");

            Update update = handle.createUpdate(query.toString());
            update.bind("count", product.getCount() - orderQuantity);
            update.bind("id", product.getId());

            return update.execute() == 1;
        });
    }
}
