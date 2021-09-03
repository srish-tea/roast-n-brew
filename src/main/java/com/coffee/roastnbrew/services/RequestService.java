package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.models.feedbacks.Request;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface RequestService {

    void sendRequest(Request request);
}

