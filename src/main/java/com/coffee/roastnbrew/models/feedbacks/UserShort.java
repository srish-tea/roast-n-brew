package com.coffee.roastnbrew.models.feedbacks;

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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserShort {
    private long id;
    private String firstName;
    private String emailId;
    private String imageUrl;
    private boolean isGroup;
}
