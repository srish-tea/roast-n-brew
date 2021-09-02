package com.coffee.roastnbrew.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize()
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Feedback extends Entity {
    long senderId;
    long receiverId;
    boolean isAnonymous;
    boolean isPublic;
    FeedbackContent content;
    List<Card> cards;
    int coins;
    boolean isVisible;  //visible to visitor
    String receiverReply;

}
