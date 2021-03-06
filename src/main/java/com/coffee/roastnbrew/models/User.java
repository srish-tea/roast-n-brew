package com.coffee.roastnbrew.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.EnumMap;
import java.util.List;
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
public class User extends Entity {
    
    private String emailId;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String designation;
    private String location;
    private String bio;
    private List<String> canTalkAbout;
    private List<String> cannotTalkAbout;
    private int coinsBalance;
    private boolean isGroup;
    private EnumMap<Card, Integer> cardCounts;
}
