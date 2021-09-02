package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.models.User;
import java.util.List;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserService {
    
    User getUserById(int id);

    List<User> getAllUsers();
    
    User updateUser(User user);
    
    User addUser(User user);
    
    boolean deleteUser(int userId);

}
