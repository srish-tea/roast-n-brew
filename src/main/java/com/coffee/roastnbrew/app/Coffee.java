package com.coffee.roastnbrew.app;

import com.coffee.roastnbrew.resources.*;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Coffee extends Application<CoffeeConfig> {

    public static void main(String[] args) {
        try {
            Coffee coffee = new Coffee();
            coffee.run(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(Bootstrap<CoffeeConfig> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addCommand(new MigrateCommand(this));
    }

    @Override
    public void run(CoffeeConfig coffeeConfig, Environment environment) throws Exception {

        environment.jersey().register(UserResource.class);
        environment.jersey().register(FeedbackResource.class);
        environment.jersey().register(MarketplaceResource.class);
        environment.jersey().register(NotificationResource.class);
        environment.jersey().register(AuthResource.class);

        final DependencyInjectionBundle dependencyInjectionBundle = new DependencyInjectionBundle();
        dependencyInjectionBundle.run(coffeeConfig, environment);

    }
}
