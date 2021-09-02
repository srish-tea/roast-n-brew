package com.coffee.roastnbrew.app;

import com.coffee.roastnbrew.resources.*;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.Driver;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.beans.PropertyVetoException;

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
    public void run(CoffeeConfig coffeeConfig, Environment environment) {

        environment.jersey().register(UserResource.class);
        environment.jersey().register(FeedbackResource.class);
        environment.jersey().register(MarketplaceResource.class);
        environment.jersey().register(NotificationResource.class);
        environment.jersey().register(AuthResource.class);
        environment.jersey().register(RequestResource.class);

        final DependencyInjectionBundle dependencyInjectionBundle = new DependencyInjectionBundle();
        dependencyInjectionBundle.run(coffeeConfig, environment);
    }

    private ComboPooledDataSource createDataSource(CoffeeConfig coffeeConfig) throws CoffeeException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass(Driver.class.getCanonicalName()); //loads the jdbc driver
        } catch (PropertyVetoException e) {
            throw new CoffeeException("Error in setting db driver", e);
        }
        comboPooledDataSource.setJdbcUrl(coffeeConfig.getDbURL());
        comboPooledDataSource.setTestConnectionOnCheckout(true);
        comboPooledDataSource.setPreferredTestQuery("SELECT 1");
        return comboPooledDataSource;
    }
}
