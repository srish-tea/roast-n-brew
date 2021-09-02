package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.models.Request;
import com.coffee.roastnbrew.models.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface RequestService {

    void sendRequest(User user, Request request);
}

