package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.User;
import com.coffee.roastnbrew.daos.UsersDAO;
import com.coffee.roastnbrew.services.UserService;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;

@Service
@Singleton
public class UserServiceImpl implements UserService {
    
    private UsersDAO usersDAO;

    @Inject
    public UserServiceImpl(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public User getUser(int userId) {
        return usersDAO.getById(userId);
    }
}
