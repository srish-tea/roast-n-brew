package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserService {

    public User getUser(int userId);
}
