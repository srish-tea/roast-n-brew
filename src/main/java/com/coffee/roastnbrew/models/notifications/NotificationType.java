package com.coffee.roastnbrew.models.notifications;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public enum NotificationType {
    RECEIVED_REQUEST,
    RECEIVED_FEEDBACK,
    GLOBAL_NOTIFICATION,
    ORDER_PLACED,
    USER_JOINED,
    FEEDBACK_REPLIED
    ;
    
    public static Map<String, List<NotificationType>> entityToTypeMap =
        ImmutableMap.<String, List<NotificationType>>builder()
            .put("product", Collections.singletonList(ORDER_PLACED))
            .put("user", Arrays.asList(RECEIVED_REQUEST, RECEIVED_FEEDBACK, GLOBAL_NOTIFICATION, USER_JOINED, FEEDBACK_REPLIED))
            .build();
}
