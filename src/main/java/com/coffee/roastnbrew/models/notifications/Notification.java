package com.coffee.roastnbrew.models.notifications;

import com.coffee.roastnbrew.models.Entity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize()
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Notification extends Entity {
    
    private Long userId;
    private NotificationType type;
    private String message;
    private boolean isRead;
    private Long entityId;
    private Long fromId;
    private String entityImageUrl;
}
