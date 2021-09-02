package com.coffee.roastnbrew.app;

import com.coffee.roastnbrew.daos.NotificationDAO;
import com.coffee.roastnbrew.daos.UsersDAO;
import com.coffee.roastnbrew.services.MarketplaceService;
import com.coffee.roastnbrew.services.NotificationService;
import com.coffee.roastnbrew.services.UserService;
import com.coffee.roastnbrew.services.impl.MarketplaceServiceImpl;
import com.coffee.roastnbrew.services.impl.NotificationServiceImpl;
import com.coffee.roastnbrew.services.impl.UserServiceImpl;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class DependencyInjectionBundle implements ConfiguredBundle<DependencyInjectionConfiguration> {

    @Override
    public void run(DependencyInjectionConfiguration configuration, Environment environment) {
        environment
                .jersey()
                .register(
                        new AbstractBinder() {
                            @Override
                            protected void configure() {
                                /*for (Class<?> singletonClass : configuration.getSingletons()) {
                                    bindAsContract(singletonClass).in(Singleton.class);
                                }*/
                                bind(UserServiceImpl.class).to(UserService.class).in(Singleton.class);
                                bind(UsersDAO.class).to(UsersDAO.class).in(Singleton.class);
                                
                                bind(MarketplaceServiceImpl.class).to(MarketplaceService.class).in(Singleton.class);
                                
                                bind(NotificationServiceImpl.class).to(NotificationService.class).in(Singleton.class);
                                bind(NotificationDAO.class).to(NotificationDAO.class).in(Singleton.class);

                                for (NamedProperty<? extends Object> namedProperty : configuration.getNamedProperties()) {
                                    bind((Object) namedProperty.getValue()).to((Class<Object>) namedProperty.getClazz()).named(namedProperty.getId());
                                }
                            }
                        }
                );
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        throw new UnsupportedOperationException();
    }
}
