package com.coffee.roastnbrew.services.impl;


import com.coffee.roastnbrew.daos.RequestDAO;
import com.coffee.roastnbrew.models.feedbacks.Request;
import com.coffee.roastnbrew.services.NotificationService;
import com.coffee.roastnbrew.services.RequestService;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;

@Service
@Singleton
public class RequestServiceImpl implements RequestService {
    
    private RequestDAO requestDAO;
    private NotificationService notificationService;
    
    @Inject
    public RequestServiceImpl(RequestDAO requestDAO, NotificationService notificationService) {
        this.requestDAO = requestDAO;
        this.notificationService = notificationService;
    }
    
    @Override
    public void sendRequest(Request request) {
        requestDAO.addRequest(request);
        
        notificationService.feedbackRequestNotification(request);
    }
}
