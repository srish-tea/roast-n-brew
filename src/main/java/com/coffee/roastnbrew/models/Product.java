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
public class Product extends Entity {
    String name = "Sipper";
    int price; // Price in coins, hence integer
    String image_url;
    int count;

}
