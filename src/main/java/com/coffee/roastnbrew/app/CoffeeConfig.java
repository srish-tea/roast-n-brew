package com.coffee.roastnbrew.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import java.util.ArrayList;
import java.util.List;


public class CoffeeConfig extends Configuration implements DependencyInjectionConfiguration {

    private String defaultName = "Roast and Brew";
    private String dbURL;

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    @JsonProperty
    public String getDbURL() {
        return dbURL;
    }

    @JsonProperty
    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    /*protected Class<?> getUserRepository() {
        return UserRepository.class;
    }*/

   /* @Override
    public List<Class<?>> getSingletons() {
        final List<Class<?>> result = new ArrayList();
        result.add(UserDBService.class);
        result.add(UserResource.class);

        return result;
    }*/

    @Override
    public List<NamedProperty<? extends Object>> getNamedProperties() {
        final List<NamedProperty<? extends Object>> result = new ArrayList<>();
        result.add(new NamedProperty<>("dbUser", "dummy_db_user", String.class));

        return result;
    }
}
