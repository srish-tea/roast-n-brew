package com.coffee.roastnbrew.resources;

import com.coffee.roastnbrew.services.NotificationService;
import com.coffee.roastnbrew.utils.RestUtils;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("/coffee/notifications")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {
    
    NotificationService notificationService;
    
    @Inject
    public NotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GET
    public Response getNotifications(@QueryParam("user_id") int userId,
        @QueryParam("unread_only") @DefaultValue("false") boolean unreadOnly) {
        return RestUtils.ok(notificationService.getNotifications(userId, unreadOnly));
    }

    @PUT
    public Response markNotificationsRead(List<Long> notificationIds) {
        return RestUtils.ok(notificationService.markNotificationsRead(notificationIds));
    }

    @POST
    public Response sendGlobalNotification(String message) {
        notificationService.sendGlobalNotification(message);
        return RestUtils.noContentResponse();
    }
}
