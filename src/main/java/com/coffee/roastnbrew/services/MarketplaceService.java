package com.coffee.roastnbrew.services;

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

    long createOrder(Order order);

    boolean decreaseProductCount(Product product, int orderQuantity);

    List<Order> getOrdersByUserId(long userId);
}
