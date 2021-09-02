package com.coffee.roastnbrew.services;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserService {

    String getUserById(int id);

    String getAllUsers();

    String updateUser();

    String addUser();
}
