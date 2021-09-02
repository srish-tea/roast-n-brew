package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.models.Product;

public interface MarketplaceService {
    String getAllProducts();

    Product getProductById(long productId);

    String addProduct(Product product);

    String getOrderById(long orderId);

    String createOrderForUser(long productId, String userId);
}
