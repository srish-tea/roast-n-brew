package com.coffee.roastnbrew.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entity {
    long id;
    boolean isDeleted;
    private long createdTs;
    private long updatedTs;
}
