package com.coffee.roastnbrew.app;

import java.util.List;

public interface DependencyInjectionConfiguration {
   // List<Class<?>> getSingletons();
    List<NamedProperty<? extends Object>> getNamedProperties();
}
