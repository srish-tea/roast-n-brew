package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.services.UserService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Named;
import javax.inject.Singleton;

@Service
@Singleton
public class UserDBService implements UserService {
    public String getUser() {
        return "Works!";
    }
}
