package com.coffee.roastnbrew.exceptions;

public class APIUnavailableException extends CoffeeException {

    public APIUnavailableException(int status) {
        super(String.format("API Unavailable. Status: %d", status));
    }
}
