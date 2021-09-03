package com.coffee.roastnbrew.models.feedbacks;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize()
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FeedbackContent {
    String startText;
    String continueText;
    String stopText;
    String message;
    String gifUrl;
}
