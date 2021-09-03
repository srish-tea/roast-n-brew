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

    @Override
    public long createOrder(Order order) throws BadRequest {
        User user = usersDAO.getById(order.getUserId());
        Product product = productDAO.getById(order.getProductId());
        if (user.getCoinsBalance() < product.getPrice()) {
            throw new BadRequest("Not enough coins!");
        }
        long id = orderDAO.createOrder(order);
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
