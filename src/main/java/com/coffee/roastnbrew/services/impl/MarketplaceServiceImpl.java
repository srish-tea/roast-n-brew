package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.models.Product;
import com.coffee.roastnbrew.services.MarketplaceService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;

@Service
@Singleton
public class MarketplaceServiceImpl implements MarketplaceService {

    @Override
    public String getAllProducts() {
        return null;
    }

    @Override
    public Product getProductById(long productId) {
        return Product.builder().name("Waah").build();
    }

    @Override
    public String addProduct(Product product) {
        return null;
    }

    @Override
    public String getOrderById(long orderId) {
        return null;
    }

    @Override
    public String createOrderForUser(long productId, String userId) {
        return null;
    }
}
