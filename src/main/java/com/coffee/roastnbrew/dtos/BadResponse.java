package com.coffee.roastnbrew.dtos;


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
public class BadResponse {
    String message;
    int code;
}
