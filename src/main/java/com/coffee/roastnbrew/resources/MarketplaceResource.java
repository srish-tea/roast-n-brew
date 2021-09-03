package com.coffee.roastnbrew.resources;

import com.coffee.roastnbrew.exceptions.BadRequest;
import com.coffee.roastnbrew.models.Product;
import com.coffee.roastnbrew.models.marketplace.Order;
import com.coffee.roastnbrew.services.MarketplaceService;
import com.coffee.roastnbrew.utils.RestUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/coffee/market")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class MarketplaceResource {
    MarketplaceService marketplaceService;

    @Inject
    public MarketplaceResource(MarketplaceService marketplaceService) {
        this.marketplaceService = marketplaceService;
    }


    @GET
    @Path("/products")
    public Response getAllProducts() {
        return RestUtils.ok(marketplaceService.getAllProducts());
    }

    @GET
    @Path("/products/{id}")
    public Response getProductById(@PathParam("id") long productId) {
        return RestUtils.ok(marketplaceService.getProductById(productId));
    }

    @POST
    @Path("/products")
    public Response addProduct(Product product) {
        marketplaceService.addProduct(product);
        return RestUtils.ok(marketplaceService.getProductById(product.getId()));
    }

    @Path("/orders/{id}")
    @GET
    public Response getOrderById(@PathParam("id") long orderId) {
        return RestUtils.ok(marketplaceService.getOrderById(orderId));
    }

    @POST
    @Path("/orders")
    public Response orderProduct(Order order) throws BadRequest {
        marketplaceService.createOrder(order);
        return RestUtils.ok(marketplaceService.getOrderById(order.getId()));
    }

    @GET
    @Path("/orders")
    public Response getUserOrders(@QueryParam("userId") long userId) {
        return RestUtils.ok(marketplaceService.getOrdersByUserId(userId));


    }
}
