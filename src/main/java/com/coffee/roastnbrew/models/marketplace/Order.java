package com.coffee.roastnbrew.models.marketplace;


import com.coffee.roastnbrew.models.Entity;
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
public class Order extends Entity {
    long userId;
    long productId;
    OrderStatus status;
}
