package com.coffee.roastnbrew.services.impl;

import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.daos.UsersDAO;
import com.coffee.roastnbrew.services.UserService;
import java.util.List;
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

    public User getUserById(long userId) {
        return usersDAO.getById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return usersDAO.getAllUsers();
    }

    @Override
    public User updateUser(User user) {
        usersDAO.updateUser(user);
        return getUserById(user.getId());
    }

    @Override
    public User addUser(User user) {
        int userId = usersDAO.addUser(user);
        return getUserById(userId);
    }
    
    @Override
    public boolean deleteUser(long userId) {
        return usersDAO.deleteUser(userId);
    }
}
