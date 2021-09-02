package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.models.Product;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface MarketplaceService {
    String getAllProducts();

    Product getProductById(long productId);

    String addProduct(Product product);

    String getOrderById(long orderId);

    String createOrderForUser(long productId, String userId);
}
