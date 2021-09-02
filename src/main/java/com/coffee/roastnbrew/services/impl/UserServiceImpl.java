package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.services.UserService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;

@Service
@Singleton
public class UserServiceImpl implements UserService {

    @Override
    public String getUserById(int id) {
        return "Works!";
    }

    @Override
    public String getAllUsers() {
        return null;
    }

    @Override
    public String updateUser() {
        return null;
    }

    @Override
    public String addUser() {
        return null;
    }
}
