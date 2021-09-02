package com.coffee.roastnbrew.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Feedback extends Entity {

}
