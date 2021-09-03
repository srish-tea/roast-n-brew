package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.daos.OrderDAO;
import com.coffee.roastnbrew.daos.ProductDAO;
import com.coffee.roastnbrew.daos.UsersDAO;
import com.coffee.roastnbrew.exceptions.BadRequest;
import com.coffee.roastnbrew.models.Product;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.models.marketplace.Order;
import com.coffee.roastnbrew.services.MarketplaceService;
import com.coffee.roastnbrew.services.NotificationService;
import com.coffee.roastnbrew.utils.JSONUtils;
import com.coffee.roastnbrew.utils.RestUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Service
@Singleton
public class MarketplaceServiceImpl implements MarketplaceService {
    
    ProductDAO productDAO;
    OrderDAO orderDAO;
    NotificationService notificationService;
    UsersDAO usersDAO;

    @Inject
    MarketplaceServiceImpl(ProductDAO productDAO, OrderDAO orderDAO, NotificationService notificationService, UsersDAO usersDAO) {
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
        this.notificationService = notificationService;
        this.usersDAO = usersDAO;
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    @Override
    public Product getProductById(long productId) {
        return productDAO.getById(productId);
    }

    @Override
    public long addProduct(Product product) {
        return productDAO.addProduct(product);
    }

    @Override
    public Order getOrderById(long orderId) {
        return orderDAO.getById(orderId);
    }

    @SneakyThrows
    @Override
    public long createOrder(Order order) {
        User user = usersDAO.getById(order.getUserId());
        Product product = productDAO.getById(order.getProductId());
        if (user.getCoinsBalance() < product.getPrice()) {
            throw new BadRequest("Not enough coins!");
        }
        long id = orderDAO.createOrder(order);

        Map<String, Object> zapDetails = new HashMap<>();
        zapDetails.put("product", product.getName());
        zapDetails.put("user", user.getFirstName());
        zapDetails.put("email_id", user.getEmailId());
        zapDetails.put("count", 1);
        zapDetails.put("ordered_time", new DateTime(System.currentTimeMillis()).toString("yyyy-MM-dd HH:mm"));

        RestUtils.request("https://hooks.zapier.com/hooks/catch/10771719/b4gz0sv/",
            RestUtils.REQUEST_METHOD_POST, JSONUtils.toJson(zapDetails), null, null);

        notificationService.orderPlacedNotification(user, product);
        productDAO.decreaseProductCount(product, 1);
        return id;
    }

    @Override
    public boolean decreaseProductCount(Product product, int orderQuantity) {
        return productDAO.decreaseProductCount(product, orderQuantity);
    }

    @Override
    public List<Order> getOrdersByUserId(long userId) {
        return orderDAO.getOrdersForUser(userId);
    }
}
