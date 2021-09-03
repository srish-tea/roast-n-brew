package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.exceptions.BadRequest;
import com.coffee.roastnbrew.models.Product;
import com.coffee.roastnbrew.models.marketplace.Order;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface MarketplaceService {
    List<Product> getAllProducts();

    Product getProductById(long productId);

    long addProduct(Product product);

    Order getOrderById(long orderId);

    long createOrder(Order order) throws BadRequest;

    List<Order> getOrdersByUserId(long userId);

    boolean decreaseProductCount(Product product, int orderQuantity);
}
