package com.coffee.roastnbrew.services.impl;


import com.coffee.roastnbrew.models.Request;
import com.coffee.roastnbrew.models.User;
import com.coffee.roastnbrew.services.RequestService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;

@Service
@Singleton
public class RequestServiceImpl implements RequestService {
    @Override
    public void sendRequest(User user, Request request) {

    }
}
