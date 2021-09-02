package com.coffee.roastnbrew.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize()
public class Entity {
    long id;
    boolean isDeleted;
    private long createdTs;
    private long updatedTs;
}
