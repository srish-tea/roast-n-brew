package com.coffee.roastnbrew.app;

import com.coffee.roastnbrew.resources.UserResource;
import io.dropwizard.Application;
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
    public void run(CoffeeConfig coffeeConfig, Environment environment) throws Exception {
        //final UserResource resource = new UserResource();
        environment.jersey().register(UserResource.class);
        final DependencyInjectionBundle dependencyInjectionBundle = new DependencyInjectionBundle();
        dependencyInjectionBundle.run(coffeeConfig, environment);
        //ServiceLocator locator =  ServiceLocatorUtilities.createAndPopulateServiceLocator();
        //UserService myService = locator.getService(UserService.class);
    }
}
